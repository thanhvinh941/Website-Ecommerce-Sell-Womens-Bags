package com.ecommerce.domain.service.impl;

import org.springframework.stereotype.Service;

import com.ecommerce.domain.db.read.RCarItemMapper;
import com.ecommerce.domain.db.write.WCarItemMapper;
import com.ecommerce.domain.service.CartService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService{
	
	private final WCarItemMapper wCarItemMapper;
	
	private final RCarItemMapper rCarItemMapper;
	
	@Override
	public String getLoadcart() {
		
		// TODO Auto-generated method stub
		return null;
	}

}
