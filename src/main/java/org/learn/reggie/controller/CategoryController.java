package org.learn.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.learn.reggie.common.R;
import org.learn.reggie.entity.Category;
import org.learn.reggie.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

	@GetMapping("/page")
	public R<Page> page(int page, int pageSize) {
		//Page constructor
		Page<Category> pageInfo = new Page<>(page, pageSize);

		LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.orderByAsc(Category::getSort);

		categoryService.page(pageInfo, queryWrapper);
		return R.success(pageInfo);
	}

	@DeleteMapping
	public R<String> delete(@RequestParam(value = "ids") Long id){
		log.info("Delete category by id {}", id);
		categoryService.remove(id);
		return R.success("Delete category success");
	}

	@PutMapping
	public R<String> update(@RequestBody Category category){
		categoryService.updateById(category);
		return R.success("Update category success");
	}
}
