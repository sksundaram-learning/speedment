/**
 *
 * Copyright (c) 2006-2016, Speedment, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); You may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.speedment.internal.ui.controller;

import com.speedment.component.notification.Notification;
import com.speedment.internal.ui.UISession;
import com.speedment.internal.ui.util.LayoutAnimator;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.FlowPane;
import static java.util.Objects.requireNonNull;

/**
 * FXML Controller class
 *
 * @author  Emil Forslund
 * @since   2.3
 */
public class NotificationAreaController implements Initializable {
    
    private final UISession session;
    private final LayoutAnimator animator;
    private @FXML FlowPane notificationArea;
    
    public NotificationAreaController(UISession session) {
        this.session  = requireNonNull(session);
        this.animator = new LayoutAnimator();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        animator.observe(notificationArea.getChildren());
        
        final ObservableList<Notification> notifications = session.getSpeedment()
                .getUserInterfaceComponent()
                .getNotifications();
        
        notifications.addListener((ListChangeListener.Change<? extends Notification> change) -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    change.getAddedSubList().forEach(n -> {
                        NotificationController.showNotification(notificationArea, n.text(), n.icon(), n.palette(), n.onClose()
                        );
                        
                        notifications.remove(n);
                    });
                }
            }
        });
        
        while (!notifications.isEmpty()) {
            final Notification n = notifications.get(0);
            NotificationController.showNotification(notificationArea, n.text(), n.icon(), n.palette(), n.onClose()
            );
            notifications.remove(0);
        }
    }
}