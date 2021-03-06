/*
 * Copyright 2002-2015 SCOOP Software GmbH
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
package org.copperengine.monitoring.client.ui.message.filter;

import javafx.beans.property.SimpleBooleanProperty;

import org.copperengine.monitoring.client.form.filter.defaultfilter.MaxCountFilterModel;
import org.copperengine.monitoring.client.form.filter.enginefilter.EnginePoolFilterModel;

public class MessageFilterModel extends EnginePoolFilterModel {
    public final SimpleBooleanProperty ignoreProcessed = new SimpleBooleanProperty();
    public final MaxCountFilterModel maxCountFilterModel = new MaxCountFilterModel();

}
