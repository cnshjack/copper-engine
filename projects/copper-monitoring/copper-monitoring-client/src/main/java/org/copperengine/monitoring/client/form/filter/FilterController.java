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
package org.copperengine.monitoring.client.form.filter;

import javafx.beans.property.ListProperty;
import javafx.scene.Node;

import org.copperengine.monitoring.client.form.FxmlController;

public interface FilterController<F> extends FxmlController {
    public static long DEFAULT_REFRESH_INTERVAL = 1000;

    public F getFilter();

    public boolean supportsFiltering();

    // public void executeFilter();
    public long getDefaultRefreshInterval();

    public ListProperty<ActionsWithFilterForm> getActionsWithFilterForm();

    public static interface ActionsWithFilterForm {
        public void run(FilterAbleForm<?, ?> filterForm);
    }

    /**
     * @return null for no filter
     */
    public Node createDefaultFilter();
}
