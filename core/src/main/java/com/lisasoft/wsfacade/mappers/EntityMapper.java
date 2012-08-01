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

import com.lisasoft.wsfacade.models.EntityModel;
import com.lisasoft.wsfacade.models.Model;

public abstract class EntityMapper extends Mapper {
	
	public Model mapToModel(String uri, String source, String contentType) throws IllegalArgumentException {
		EntityModel model = (EntityModel)mapToModel(source);

		model.contentType = contentType;
		model.isBinary = false;
		model.textSource = source;

		return model;
	}
	public Model mapToModel(String source, String contentType) throws IllegalArgumentException {
		EntityModel model = (EntityModel)mapToModel(source);

		model.contentType = contentType;
		model.isBinary = false;
		model.textSource = source;

		return model;
	}

	public Model mapToModel(byte[] source, String contentType) throws IllegalArgumentException {
		EntityModel model = (EntityModel)mapToModel(null);

		model.contentType = contentType;
		model.isBinary = true;
		model.binarySource = source;
		
		return model;
	}
}
