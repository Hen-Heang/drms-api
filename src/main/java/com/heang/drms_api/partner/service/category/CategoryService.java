package com.heang.drms_api.partner.service.category;

import com.heang.drms_api.partner.model.Category;
import org.apache.ibatis.javassist.NotFoundException;

import java.text.ParseException;

public interface CategoryService {

    Category createCategories(String categoryName) throws NotFoundException, ParseException;
}
