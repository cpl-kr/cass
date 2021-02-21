package de.platen.clapsesy.guiserver.start;

import java.awt.Dimension;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.java_websocket.WebSocket;

import de.platen.clapsesy.guiserver.exception.GuiServerException;
import de.platen.clapsesy.guiserver.frontend.ApplicationWindow;
import de.platen.clapsesy.guiserver.frontend.ElementId;
import de.platen.clapsesy.guiserver.frontend.GuiElementVerwaltungOperation;
import de.platen.clapsesy.guiserver.frontend.Height;
import de.platen.clapsesy.guiserver.frontend.Width;
import de.platen.clapsesy.guiserver.frontend.guielement.ElementPosition;
import de.platen.clapsesy.guiserver.frontend.guielement.Event;
import de.platen.clapsesy.guiserver.frontend.guielement.GuiElement;
import de.platen.clapsesy.guiserver.frontend.guielement.State;
import de.platen.clapsesy.guiserver.frontend.guielement.View;
import de.platen.clapsesy.guiserver.frontend.guielement.images.ImageCache;
import de.platen.clapsesy.guiserver.renderer.HtmlRenderEngine;
import de.platen.clapsesy.guiserver.schema.ChangeElementType;
import de.platen.clapsesy.guiserver.schema.ElementOperationType;
import de.platen.clapsesy.guiserver.schema.ElementType;
import de.platen.clapsesy.guiserver.schema.GUI;
import de.platen.clapsesy.guiserver.schema.OperationType;
import de.platen.clapsesy.guiserver.schema.SourceType;
import de.platen.clapsesy.guiserver.schema.StateType;
import de.platen.clapsesy.guiserver.schema.ViewType;
import javafx.application.Platform;

public class GuiBehandlung {

    private static final Map<UUID, List<ApplicationWindow>> APPLICATIONWINDOWS = new HashMap<>();

    private final GuiElementFactory guiElementFactory;
    private final GuiAufbau guiAufbau;
    private final HtmlRenderEngine htmlRenderEngine;
    private final SessionVerwaltung sessionVerwaltung;
    private final GuiElementVerwaltungOperation guiElementVerwaltungOperation;

    public GuiBehandlung(final GuiElementFactory guiElementFactory, final GuiAufbau guiAufbau,
            final HtmlRenderEngine htmlRenderEngine, final SessionVerwaltung sessionVerwaltung,
            final GuiElementVerwaltungOperation guiElementVerwaltungOperation) {
        this.guiElementFactory = guiElementFactory;
        this.guiAufbau = guiAufbau;
        this.htmlRenderEngine = htmlRenderEngine;
        this.sessionVerwaltung = sessionVerwaltung;
        this.guiElementVerwaltungOperation = guiElementVerwaltungOperation;
    }

    public void baueInitialGUI(final GUI gui, final WebSocket connection) {
        final Runnable runnable = () -> {
            final UUID sessionId = UUID.fromString(gui.getSessionId());
            if (gui.getType().getInitialGUI() != null) {
                final List<ApplicationWindow> windows = guiAufbau.baueFenster(gui, sessionId, connection,
                        htmlRenderEngine);
                APPLICATIONWINDOWS.put(sessionId, windows);
                for (final ApplicationWindow window : windows) {
                    window.show();
                }
                System.out.println("Initial-GUI ist aufgebaut.");
            }
        };
        Platform.runLater(runnable);
    }

    public void changeGUI(final GUI gui, final WebSocket connection) {
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final UUID sessionId = UUID.fromString(gui.getSessionId());
                if (gui.getType().getChangeGUI() != null) {
                    final List<OperationType> operations = gui.getType().getChangeGUI().getOperation();
                    for (final OperationType operation : operations) {
                        if (operation.getRemoveElement() != null) {
                            System.out.println("removeElement.");
                            removeElement(sessionId, operation);
                        }
                        if (operation.getRemoveWindow() != null) {
                            System.out.println("removeWindow.");
                            removeWindow(sessionId, operation);
                        }
                        if (operation.getAddElement() != null) {
                            System.out.println("addElement.");
                            addElement(sessionId, operation, htmlRenderEngine);
                        }
                        if (operation.getAddWindow() != null) {
                            System.out.println("addWindow.");
                            addWindow(sessionId, operation, htmlRenderEngine);
                        }
                        if (operation.getChangeElement() != null) {
                            System.out.println("changeElement.");
                            final String path = sessionVerwaltung.getPath(sessionId);
                            changeElement(sessionId, operation, htmlRenderEngine, path);
                        }
                    }
                }
            }

            private void removeElement(final UUID sessionId, final OperationType operation) {
                final String windowId = operation.getRemoveElement().getWindowID();
                final ElementId elementId = new ElementId(operation.getRemoveElement().getElementID());
                final List<ApplicationWindow> windows = APPLICATIONWINDOWS.get(sessionId);
                if (windows != null) {
                    boolean found = false;
                    for (final ApplicationWindow window : windows) {
                        if (window.getId().equals(windowId)) {
                            found = true;
                            window.removeElement(elementId);
                        }
                    }
                    if (!found) {
                        throw new GuiServerException("Element not found.");
                    }
                } else {
                    throw new GuiServerException("Element not found.");
                }
            }

            private void removeWindow(final UUID sessionId, final OperationType operation) {
                final String windowId = operation.getRemoveWindow().getWindowID();
                final List<ApplicationWindow> windows = APPLICATIONWINDOWS.get(sessionId);
                if (windows != null) {
                    ApplicationWindow windowToRemove = null;
                    for (final ApplicationWindow window : windows) {
                        if (window.getId().equals(windowId)) {
                            if (windowToRemove == null) {
                                window.close();
                                windowToRemove = window;
                            }
                        }
                    }
                    if (windowToRemove != null) {
                        windows.remove(windowToRemove);
                        if (windows.isEmpty()) {
                            APPLICATIONWINDOWS.remove(sessionId);
                        }
                    } else {
                        throw new GuiServerException("Window not found.");
                    }
                } else {
                    throw new GuiServerException("Window not found.");
                }
            }

            private void addElement(final UUID sessionId, final OperationType operation,
                    final HtmlRenderEngine htmlRenderEngine) {
                final String windowId = operation.getAddElement().getWindowID();
                final ElementType element = operation.getAddElement().getElement();
                final List<ApplicationWindow> windows = APPLICATIONWINDOWS.get(sessionId);
                if (windows != null) {
                    final GuiElement guiElement = guiElementFactory.createGuiElement(element, htmlRenderEngine,
                            guiElementVerwaltungOperation);
                    for (final ApplicationWindow window : windows) {
                        if (window.getId().equals(windowId)) {
                            window.addElement(guiElement);
                        }
                    }
                } else {
                    throw new GuiServerException("Window not found.");
                }
            }

            private void addWindow(final UUID sessionId, final OperationType operation,
                    final HtmlRenderEngine htmlRenderEngine) {
                final ApplicationWindow applicationWindow = guiAufbau.baueEinzelfenster(sessionId, connection,
                        operation.getAddWindow().getWindow(), htmlRenderEngine);
                final List<ApplicationWindow> windows = APPLICATIONWINDOWS.get(sessionId);
                if (windows != null) {
                    windows.add(applicationWindow);
                } else {
                    final List<ApplicationWindow> windowList = new ArrayList<>();
                    windowList.add(applicationWindow);
                    APPLICATIONWINDOWS.put(sessionId, windowList);
                }
                applicationWindow.show();
            }

            private void changeElement(final UUID sessionId, final OperationType operation,
                    final HtmlRenderEngine htmlRenderEngine, final String path) {
                boolean found = false;
                final ChangeElementType changeElementType = operation.getChangeElement();
                final String windowId = changeElementType.getWindowID();
                final ElementId elementId = new ElementId(changeElementType.getElementID());
                final ElementOperationType elementOperationType = changeElementType.getOperation();
                if (elementOperationType.getChangeState() != null) {
                    final StateType stateType = elementOperationType.getChangeState().getState();
                    final List<ApplicationWindow> windows = APPLICATIONWINDOWS.get(sessionId);
                    if (windows != null) {
                        for (final ApplicationWindow window : windows) {
                            if (window.getId().equals(windowId)) {
                                found = true;
                                final State state = guiAufbau.convertToState(stateType);
                                window.changeElementState(elementId, state);
                            }
                        }
                    }
                }
                if (elementOperationType.getReload() != null) {
                    final ViewType viewType = elementOperationType.getReload().getView();
                    final List<ApplicationWindow> windows = APPLICATIONWINDOWS.get(sessionId);
                    if (windows != null) {
                        for (final ApplicationWindow window : windows) {
                            if (window.getId().equals(windowId)) {
                                found = true;
                                final View view = guiAufbau.convertToView(viewType);
                                final ElementPosition elementPosition = window.getElementPosition(elementId);
                                if (elementPosition != null) {
                                    try {
                                        final HtmlSource htmlSource = createHtmlSource(htmlRenderEngine,
                                                elementOperationType.getReload().getImage(), elementPosition.getWidth(),
                                                elementPosition.getHeight(), path);
                                        final ImageCache image = new ImageCache(htmlSource);
                                        window.setElementImage(elementId, view, image);
                                    } catch (final GuiServerException e) {
                                        System.out.println("Fehler bei Reload.");
                                    }
                                }
                            }
                        }
                    }
                }
                if (elementOperationType.getEvent() != null) {
                    final List<ApplicationWindow> windows = APPLICATIONWINDOWS.get(sessionId);
                    if (windows != null) {
                        for (final ApplicationWindow window : windows) {
                            if (window.getId().equals(windowId)) {
                                found = true;
                                final Event event = guiElementFactory
                                        .convertToEvent(elementOperationType.getEvent().getType());
                                window.setEvent(elementId, event, elementOperationType.getEvent().isActive());
                            }
                        }
                    }
                }
                if (!found) {
                    throw new GuiServerException("Element not found.");
                }
            }

            private HtmlSource createHtmlSource(final HtmlRenderEngine htmlRenderEngine, final SourceType sourceType,
                    final Width width, final Height height, final String path) {
                if (sourceType.getURL() != null) {
                    final String url = sourceType.getURL();
                    return new HtmlSource(htmlRenderEngine, url, width.get(), height.get());
                }
                if (sourceType.getFile() != null) {
                    String file = sourceType.getFile();
                    if (path != null) {
                        file = path + file;
                    }
                    return new HtmlSource(htmlRenderEngine, new File(file), width.get(), height.get());
                }
                if (sourceType.getHtmlBase64() != null) {
                    final byte[] dataBytes = sourceType.getHtmlBase64();
                    return new HtmlSource(htmlRenderEngine, dataBytes, width.get(), height.get());
                }
                return null;
            }

        };
        Platform.runLater(runnable);
    }

    public void exit(final GUI gui) {
        final Runnable runnable = () -> {
            final UUID sessionId = UUID.fromString(gui.getSessionId());
            if (gui.getType().getExit() != null) {
                final List<ApplicationWindow> windows = APPLICATIONWINDOWS.get(sessionId);
                for (final ApplicationWindow window : windows) {
                    window.close();
                }
                APPLICATIONWINDOWS.remove(sessionId);
                System.out.println("Exit.");
            }
        };
        Platform.runLater(runnable);
    }

    public void getResolution(final InfoResolution info) {
        final Runnable runnable = () -> {
            final Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
            final double width = screenSize.getWidth();
            final double height = screenSize.getHeight();
            info.setX((int) width);
            info.setY((int) height);
        };
        Platform.runLater(runnable);
    }
}
