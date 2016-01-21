package net.scrumplex.sprummlbot;

import com.github.theholywaffle.teamspeak3.api.CommandFuture;
import com.github.theholywaffle.teamspeak3.api.event.*;
import com.github.theholywaffle.teamspeak3.api.wrapper.ClientInfo;
import net.scrumplex.sprummlbot.configurations.Messages;
import net.scrumplex.sprummlbot.plugins.SprummlEventType;
import net.scrumplex.sprummlbot.plugins.SprummlbotPlugin;
import net.scrumplex.sprummlbot.tools.Exceptions;
import net.scrumplex.sprummlbot.vpn.VPNChecker;

/**
 * This class handles the events.
 */
class Events {

    /**
     * Registers the events
     */
    public static void start() {
        Vars.API.addTS3Listeners(new TS3Listener() {
            public void onTextMessage(final TextMessageEvent e) {

                if (e.getInvokerId() != Vars.QID) {
                    final String message = e.getMessage().toLowerCase().replace("<video", "");
                    Vars.API.getClientInfo(e.getInvokerId()).onSuccess(new CommandFuture.SuccessListener<ClientInfo>() {
                        @Override
                        public void handleSuccess(ClientInfo c) {
                            if (message.startsWith("!")) {
                                if (!Commands.handle(message, c)) {
                                    Vars.API.sendPrivateMessage(c.getId(), Messages.get("unknown-command"));
                                }
                                System.out.println(message + " received from " + e.getInvokerName());
                            } else {
                                for (SprummlbotPlugin plugin : Main.pluginManager.getPlugins()) {
                                    try {
                                        plugin.onEvent(SprummlEventType.MESSAGE, e);
                                    } catch (Exception ex) {
                                        Exceptions.handlePluginError(ex, plugin);
                                    }
                                }
                            }
                        }
                    });
                }
            }

            public void onServerEdit(final ServerEditedEvent e) {
                for (SprummlbotPlugin plugin : Main.pluginManager.getPlugins()) {
                    try {
                        plugin.onEvent(SprummlEventType.VIRTUAL_SERVER_EDIT, e);
                    } catch (Exception ex) {
                        Exceptions.handlePluginError(ex, plugin);
                    }
                }

                if (Vars.QID != e.getInvokerId()) {
                    Vars.API.getClientInfo(e.getInvokerId()).onSuccess(new CommandFuture.SuccessListener<ClientInfo>() {
                        @Override
                        public void handleSuccess(ClientInfo cl) {
                            System.out.println("The user " + e.getInvokerName() + " edited the Server! User info: uid="
                                    + cl.getUniqueIdentifier() + " ip=" + cl.getIp() + " country=" + cl.getCountry() + ".");
                        }
                    });
                }
            }

            public void onClientMoved(final ClientMovedEvent e) {
                for (SprummlbotPlugin plugin : Main.pluginManager.getPlugins()) {
                    try {
                        plugin.onEvent(SprummlEventType.CLIENT_MOVE, e);
                    } catch (Exception ex) {
                        Exceptions.handlePluginError(ex, plugin);
                    }
                }
            }

            public void onClientLeave(final ClientLeaveEvent e) {
                for (SprummlbotPlugin plugin : Main.pluginManager.getPlugins()) {
                    try {
                        plugin.onEvent(SprummlEventType.CLIENT_LEAVE, e);
                    } catch (Exception ex) {
                        Exceptions.handlePluginError(ex, plugin);
                    }
                }
            }

            public void onClientJoin(final ClientJoinEvent e) {
                for (SprummlbotPlugin plugin : Main.pluginManager.getPlugins()) {
                    try {
                        plugin.onEvent(SprummlEventType.CLIENT_JOIN, e);
                    } catch (Exception ex) {
                        Exceptions.handlePluginError(ex, plugin);
                    }
                }
                Vars.API.sendPrivateMessage(e.getClientId(), Messages.get("welcome").replace("%client-username%", e.getClientNickname()));
                Vars.API.sendPrivateMessage(e.getClientId(), Messages.get("commandslist").replace("%commands%", Commands.AVAILABLE_COMMANDS));
                Vars.API.getClientInfo(e.getClientId()).onSuccess(new CommandFuture.SuccessListener<ClientInfo>() {
                    @Override
                    public void handleSuccess(ClientInfo result) {
                        VPNChecker check = new VPNChecker(result);
                        if (check.isBlocked()) {
                            System.out.println("[VPN Checker] " + result.getNickname() + " was kicked. VPN Type: " + check.getType() + " Blacklisted IP: " + result.getIp());
                            Vars.API.kickClientFromServer(Messages.get("you-are-using-vpn"), result.getId());
                        }
                    }
                });
            }

            public void onChannelEdit(final ChannelEditedEvent e) {
                for (SprummlbotPlugin plugin : Main.pluginManager.getPlugins()) {
                    try {
                        plugin.onEvent(SprummlEventType.CHANNEL_EDIT, e);
                    } catch (Exception ex) {
                        Exceptions.handlePluginError(ex, plugin);
                    }
                }
            }

            public void onChannelDescriptionChanged(final ChannelDescriptionEditedEvent e) {
                for (SprummlbotPlugin plugin : Main.pluginManager.getPlugins()) {
                    try {
                        plugin.onEvent(SprummlEventType.CHANNEL_DESC_CHANGED, e);
                    } catch (Exception ex) {
                        Exceptions.handlePluginError(ex, plugin);
                    }
                }
            }

            public void onChannelCreate(final ChannelCreateEvent e) {
                for (SprummlbotPlugin plugin : Main.pluginManager.getPlugins()) {
                    try {
                        plugin.onEvent(SprummlEventType.CHANNEL_CREATE, e);
                    } catch (Exception ex) {
                        Exceptions.handlePluginError(ex, plugin);
                    }
                }
            }

            public void onChannelDeleted(final ChannelDeletedEvent e) {
                for (SprummlbotPlugin plugin : Main.pluginManager.getPlugins()) {
                    try {
                        plugin.onEvent(SprummlEventType.CHANNEL_DELETE, e);
                    } catch (Exception ex) {
                        Exceptions.handlePluginError(ex, plugin);
                    }
                }
            }

            public void onChannelMoved(final ChannelMovedEvent e) {
                for (SprummlbotPlugin plugin : Main.pluginManager.getPlugins()) {
                    try {
                        plugin.onEvent(SprummlEventType.CHANNEL_MOVE, e);
                    } catch (Exception ex) {
                        Exceptions.handlePluginError(ex, plugin);
                    }
                }
            }

            public void onChannelPasswordChanged(final ChannelPasswordChangedEvent e) {
                for (SprummlbotPlugin plugin : Main.pluginManager.getPlugins()) {
                    try {
                        plugin.onEvent(SprummlEventType.CHANNEL_PW_CHANGED, e);
                    } catch (Exception ex) {
                        Exceptions.handlePluginError(ex, plugin);
                    }
                }
            }

            @Override
            public void onPrivilegeKeyUsed(PrivilegeKeyUsedEvent e) {
                for (SprummlbotPlugin plugin : Main.pluginManager.getPlugins()) {
                    try {
                        plugin.onEvent(SprummlEventType.PRIVILEGE_KEY_USED, e);
                    } catch (Exception ex) {
                        Exceptions.handlePluginError(ex, plugin);
                    }
                }
            }
        });
    }
}