/********************************************************************************
 * Copyright (c) 2019 EclipseSource and others.
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

import java.util.List;
import java.util.Optional;

import org.eclipse.glsp.api.action.Action;
import org.eclipse.glsp.api.action.kind.ExecuteServerCommandAction;
import org.eclipse.glsp.api.handler.ServerCommandHandler;
import org.eclipse.glsp.api.model.GraphicalModelState;
import org.eclipse.glsp.api.provider.ServerCommandHandlerProvider;

import com.google.inject.Inject;

public class ExecuteServerCommandActionHandler extends AbstractActionHandler {
   @Inject
   protected ServerCommandHandlerProvider commandHandlerProvider;

   @Override
   public boolean handles(final Action action) {
      return action instanceof ExecuteServerCommandAction;
   }

   @Override
   public List<Action> execute(final Action action, final GraphicalModelState modelState) {
      if (action instanceof ExecuteServerCommandAction) {
         ExecuteServerCommandAction commandAction = (ExecuteServerCommandAction) action;
         Optional<ServerCommandHandler> handler = commandHandlerProvider.getHandler(commandAction.getCommandId());
         if (handler.isPresent()) {
            return handler.get().execute(commandAction.getCommandId(), commandAction.getOptions(), modelState);
         }
      }
      return none();
   }

}
