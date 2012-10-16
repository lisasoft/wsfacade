/*
 * Copyright 2012 LISAsoft - lisasoft.com. 
 * All rights reserved.
 *
 * This file is part of Web Services Facade.
 *
 * Web Services Facade is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Web Services Facade is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Web Services Facade.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.lisasoft.wsfacade.mappers;

import com.lisasoft.wsfacade.models.IModel;


public abstract class AbstractMapper implements IMapper {
	
	public IModel mapToModel(String source, String contentType, String host) throws IllegalArgumentException {
		IModel model = mapToModel(source, "");

		model.setContentType(contentType);
		model.setBinary(false);
		model.setTextSource(source);

		return model;
	}

	public IModel mapToModel(byte[] source, String contentType, String host) throws IllegalArgumentException {
		IModel model = mapToModel(null, "");

		model.setContentType(contentType);
		model.setBinary(true);
		model.setBinarySource(source);

		return model;
	}
}
