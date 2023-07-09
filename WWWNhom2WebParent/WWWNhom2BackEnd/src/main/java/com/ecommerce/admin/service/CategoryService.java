package com.ecommerce.admin.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ecommerce.admin.category.CategoryPageInfo;
import com.ecommerce.admin.exception.CategoryNotFoudException;
import com.ecommerce.admin.repository.CategoryRepository;
import com.ecommerce.common.entity.Category;

@Service
@Transactional
public class CategoryService {
	public static final int ROOT_CATEGORIES_PER_PAGE = 1;

	@Autowired
	private CategoryRepository categoryRepository;

	public List<Category> listByPage(CategoryPageInfo pageInfo, int pageNumber, String sortDir, String sortField,
			String keyword) {
		Sort sort = Sort.by(sortField);
		if (sortDir.equals("asc")) {
			sort = sort.ascending();
		} else if (sortDir.equals("desc")) {
			sort = sort.descending();
		}

		Pageable pageable = PageRequest.of(pageNumber - 1, ROOT_CATEGORIES_PER_PAGE, sort);
		Page<Category> pageCategories = null;
		if (keyword != null && !keyword.isEmpty()) {
			pageCategories = categoryRepository.search(keyword, pageable);
		} else {
			pageCategories = categoryRepository.findRootCategories(pageable);
		}
		List<Category> rootCategories = pageCategories.getContent();

		pageInfo.setTotalElements(pageCategories.getTotalElements());
		pageInfo.setTotalPages(pageCategories.getTotalPages());

		if (keyword != null && !keyword.isEmpty()) {
			List<Category> searchResult = pageCategories.getContent();
			for (Category category : searchResult) {
				category.setHasChildren(category.getChildren().size() > 0);
			}
			return searchResult;
		} else {
			return listHierarchicalCategories(rootCategories, sortDir);
		}
	}

	private List<Category> listHierarchicalCategories(List<Category> rootCategories, String sortDir) {
		List<Category> hierarchicalCategories = new ArrayList<Category>();

		for (Category rootCategory : rootCategories) {
			hierarchicalCategories.add(Category.copyFull(rootCategory));

			Set<Category> children = sortSubCategories(rootCategory.getChildren(), sortDir);
			for (Category subCategory : children) {
				String name = "--" + subCategory.getName();
				hierarchicalCategories.add(Category.copyFull(subCategory, name));
				listSubHieraChicalCategories(hierarchicalCategories, subCategory, 1, sortDir);
			}
		}
		return hierarchicalCategories;
	}

	private void listSubHieraChicalCategories(List<Category> hierarchicalCategories, Category parent, int subLevel,
			String sortDir) {
		Set<Category> children = sortSubCategories(parent.getChildren(), sortDir);
		int newSubLevel = subLevel + 1;

		for (Category subCategory : children) {
			String name = "";
			for (int i = 0; i < newSubLevel; i++) {
				name += "--";
			}
			name += subCategory.getName();

			hierarchicalCategories.add(Category.copyFull(subCategory, name));

			listSubHieraChicalCategories(hierarchicalCategories, subCategory, newSubLevel, sortDir);
		}
	}

	public Category save(Category category) {
		Category parent = category.getParent();
		if(parent != null) {
			String allParentIds = parent.getAllParentIDs() == null ? "-" : parent.getAllParentIDs();
			allParentIds += String.valueOf(parent.getId()) + "-";
			category.setAllParentIDs(allParentIds);
		}
		
		return categoryRepository.save(category);
	}

	public List<Category> listCategoriesUsedInForm() {
		List<Category> categoriesUsedInForm = new ArrayList<Category>();
		Iterable<Category> catrgoriesInDB = categoryRepository.findRootCategories(Sort.by("id").ascending());

		for (Category category : catrgoriesInDB) {
			if (category.getParent() == null) {
				categoriesUsedInForm.add(Category.copyIdAndName(category));
				Set<Category> children = sortSubCategories(category.getChildren());
				for (Category subCategory : children) {
					String name = "--" + subCategory.getName();
					categoriesUsedInForm.add(Category.copyIdAndName(subCategory.getId(), name));
					listSubCategoriesUseInForm(categoriesUsedInForm, subCategory, 1);
				}
			}
		}

		return categoriesUsedInForm;
	}

	private void listSubCategoriesUseInForm(List<Category> categoriesUsedInForm, Category parent, int supLevel) {
		int newSubLevel = supLevel + 1;
		Set<Category> children = sortSubCategories(parent.getChildren());
		for (Category subCategory : children) {
			String name = "";
			for (int i = 0; i < newSubLevel; i++) {
				name += "--";
			}
			name += subCategory.getName();

			categoriesUsedInForm.add(Category.copyIdAndName(subCategory.getId(), name));

			listSubCategoriesUseInForm(categoriesUsedInForm, subCategory, newSubLevel);
		}
	}

	public Category get(Integer id) throws CategoryNotFoudException {
		try {
			return categoryRepository.findById(id).get();
		} catch (Exception e) {
			throw new CategoryNotFoudException("Cound not find nay category whit ID:" + id);
		}
	}

	public String checkUnique(Integer id, String name, String alias) {
		boolean isCreatingNew = (id == null || id == 0);
		Category categoryName = categoryRepository.findByName(name);

		if (isCreatingNew) {
			if (categoryName != null) {
				return "DuplicateName";
			} else {
				Category categoryAlias = categoryRepository.findByAlias(alias);
				if (categoryAlias != null) {
					return "DuplicateAlias";
				}
			}
		} else {
			if (categoryName != null && categoryName.getId() != id) {
				return "DuplicateName";
			}
			Category categoryAlias = categoryRepository.findByAlias(alias);
			if (categoryAlias != null && categoryAlias.getId() != id) {
				return "DuplicateAlias";
			}
		}

		return "OK";
	}

	private SortedSet<Category> sortSubCategories(Set<Category> childent) {
		return sortSubCategories(childent, "asc");
	}

	private SortedSet<Category> sortSubCategories(Set<Category> childent, String sortDir) {
		SortedSet<Category> sortedChildent = new TreeSet<>(new Comparator<Category>() {

			@Override
			public int compare(Category cat1, Category cat2) {

				if (sortDir.equals("asc")) {
					return cat1.getName().compareTo(cat2.getName());
				} else {
					return cat2.getName().compareTo(cat1.getName());
				}
			}
		});

		sortedChildent.addAll(childent);
		return sortedChildent;
	}

	public void updateCategoryEnabledStatus(Integer id, boolean enabled) {
		categoryRepository.updateEnabledStatus(id, enabled);
	}

	public void delete(Integer id) throws CategoryNotFoudException {
		Long countById = categoryRepository.countById(id);
		if (countById == null || countById == 0) {
			throw new CategoryNotFoudException("Could not find any category with ID " + id);
		}

		categoryRepository.deleteById(id);
	}

	public long getCount() {
		return categoryRepository.count();
	}
}
