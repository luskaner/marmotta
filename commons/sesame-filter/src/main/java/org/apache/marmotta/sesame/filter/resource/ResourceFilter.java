/**
 * Copyright (C) 2013 Salzburg Research.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.marmotta.sesame.filter.resource;

import org.apache.marmotta.sesame.filter.SesameFilter;
import org.openrdf.model.Resource;

/**
 * A common interface for enhancement filters on resources. Specifies a single method, "accept", which should
 * return false in case the filter does not accept the resource passed as argument.
 * <p/>
 * Note that enhancement filters should only implement methods that are not expensive to compute, since they will be
 * evaluated when the transaction commits and before adding a resource to the enhancement queue.
 * <p/>
 * Author: Sebastian Schaffert
 */
public interface ResourceFilter extends SesameFilter<Resource> {

}