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
package com.lisasoft.wsfacade.utils;

public class ClassUtil {

	/***
     * Load a class instance
     * 
     * @param fullClassName The name of the class that has to be loaded. Specify the full package name and 
     * separate it using '.', e.g.: "com.lisasoft.wsfacade.mappers.WmtsRestMapper"
     * 
     * @return Object: null if an error has occurred. Just cast the returned object to the one you need. 
     */
    public static Object loadClassInstance(String fullClassName) throws IllegalArgumentException {
        Object result = null;

        Class<?> classToUse;

        try {
			classToUse = Class.forName(fullClassName);
			result = classToUse.newInstance();
        } catch(ClassNotFoundException cnfe) {
			throw new IllegalArgumentException("'" + fullClassName + "' not found.", cnfe);
		} catch(InstantiationException ie) {
			throw new IllegalArgumentException("Couldn't instantiate '" + fullClassName + "'.", ie);
		} catch(IllegalAccessException iae) {
			throw new IllegalArgumentException("Instantiating '" + fullClassName + "' caused an illegal access exception.", iae);
		}
        return result;
    }

}
