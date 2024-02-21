package me.trae.clans.worldevent.interfaces;

import me.trae.framework.shared.utility.interfaces.duration.IExpire;
import me.trae.framework.shared.utility.interfaces.duration.IGetDuration;
import me.trae.framework.shared.utility.interfaces.duration.IGetSystemTime;
import me.trae.framework.shared.utility.interfaces.duration.IRemaining;

public interface IWorldEvent extends IGetSystemTime, IGetDuration, IRemaining, IExpire {

    void start();

    void stop();

    boolean isActive();
}