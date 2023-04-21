package org.learn.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.learn.reggie.entity.Category;

public interface CategoryService extends IService<Category> {
    public void remove(Long id);
}
