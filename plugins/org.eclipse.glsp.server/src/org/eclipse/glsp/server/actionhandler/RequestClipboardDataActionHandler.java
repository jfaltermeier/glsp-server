/********************************************************************************
 * Copyright (c) 2020 EclipseSource and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the Eclipse
 * Public License v. 2.0 are satisfied: GNU General Public License, version 2
 * with the GNU Classpath Exception which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 ********************************************************************************/
package org.eclipse.glsp.server.actionhandler;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.eclipse.glsp.api.action.Action;
import org.eclipse.glsp.api.action.kind.RequestClipboardDataAction;
import org.eclipse.glsp.api.action.kind.SetClipboardDataAction;
import org.eclipse.glsp.api.factory.GraphGsonConfiguratorFactory;
import org.eclipse.glsp.api.model.GraphicalModelState;
import org.eclipse.glsp.graph.GModelElement;
import org.eclipse.glsp.graph.GModelIndex;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.inject.Inject;

public class RequestClipboardDataActionHandler extends AbstractActionHandler {

   protected final Gson gson;

   @Inject
   public RequestClipboardDataActionHandler(final GraphGsonConfiguratorFactory gsonFactory) {
      GsonBuilder builder = gsonFactory.configureGson();
      gson = builder.create();
   }

   @Override
   public boolean handles(final Action action) {
      return action instanceof RequestClipboardDataAction;
   }

   @Override
   public List<Action> execute(final Action plainAction, final GraphicalModelState modelState) {
      RequestClipboardDataAction action = (RequestClipboardDataAction) plainAction;
      JsonArray jsonArray = new JsonArray();
      GModelIndex index = modelState.getIndex();
      Set<GModelElement> selectedElements = index.getAll(action.getEditorContext().getSelectedElementIds());
      selectedElements.stream().map(gson::toJsonTree).forEach(jsonArray::add);
      return listOf(new SetClipboardDataAction(Collections.singletonMap("application/json", jsonArray.toString())));
   }

}
