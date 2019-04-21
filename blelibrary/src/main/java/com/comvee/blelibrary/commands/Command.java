package com.comvee.blelibrary.commands;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;

import com.comvee.blelibrary.BluetoothMode;

/**
 * 命令超类
 * Created by 黄嘉晖 on 2017/5/8.
 */

public abstract class Command {

    private Command mNext;
    public BluetoothMode bluetoothMode;
    /**
     * 是否自动执行
     */
    private boolean isAutoRun = true;

    public boolean isAutoRun() {
        return isAutoRun;
    }

    public void setAutoRun(boolean autoRun) {
        isAutoRun = autoRun;
    }

    public Command(Command paramCommand, BluetoothMode bluetoothMode, boolean isAutoRun)
    {
        this.mNext = paramCommand;
        this.bluetoothMode = bluetoothMode;
        this.isAutoRun = isAutoRun;
    }

    public abstract void execute(BluetoothGatt paramBluetoothGatt);


    /**
     * 当前命令 再插入一条
     * @param command
     */
    public void insertCommand(Command command)
    {
        Command tempCommand = command;
        while(tempCommand.next() != null) {
            tempCommand = tempCommand.next();
        }
        tempCommand.setNext(next());
        setNext(command);
    }


    /**
     * 返回下一个命令
     * @return
     */
    public Command next()
    {
        return this.mNext;
    }

    /**
     * 设置下一条命令
     * @param paramCommand
     */
    public void setNext(Command paramCommand)
    {
        this.mNext = paramCommand;
    }

    //public abstract boolean shouldMoveToNext(Object paramObject, int paramInt1, int paramInt2);

    /**
     * @return
     */
    public String toString()
    {
        Object localObject = this;
        String str = "" + localObject.getClass().getSimpleName();
            int i = 1;
            for (Command localCommand = ((Command)localObject).next(); (localCommand != null) && (localObject.getClass().getSimpleName().equals(localCommand.getClass().getSimpleName())); localCommand = ((Command)localObject).next())
            {
                i++;
                localObject = localCommand;
                if (localCommand != null) {
                    str = str + " -> " + localCommand.getClass().getSimpleName();
                }
            }
            if (i > 1) {
                str = str + "(" + i + ")";
            }
        return str;
    }

}
