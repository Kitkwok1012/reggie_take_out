package org.learn.reggie.controller;

import lombok.extern.slf4j.Slf4j;
import org.learn.reggie.common.R;
import org.learn.reggie.entity.Category;
import org.learn.reggie.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {
	@Autowired
	private CategoryService categoryService;

	@PostMapping
	public R<String> save(@RequestBody Category category) {
		categoryService.save(category);
		log.info("category {}", category);
		return R.success("Add new category success");
	}
}
