package com.lunapps.models.auction;

import lombok.Getter;

@Getter
public enum AuctionStatus {
    HOMEY_ACCEPT("homey_accept"),
    HOMEY_DECLINE("decline"),
    SYSTEM_AUTO_CONFIRMATION("system_auto_confirmation"),
    LABORER_MADE_PROPOSE("laborer_made_propose_InProgress"),
    HOMEY_CANCEL("homey_cancel"),
    LABORER_CANCEL("laborer_cancel"),
    SYSTEM_CANCEL("cancel");

    private final String actionStatus;

    AuctionStatus(final String actionStatus) {
        this.actionStatus = actionStatus;
    }
}
