package com.comvee.blelibrary.commands;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.os.Build;

import com.comvee.blelibrary.BluetoothMode;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by xiaoxinI2000 on 2017/5/9.
 */

public class SendPremiumModeCommand extends Command {
    private GroupCommandListener groupCommandListener;
    public SendPremiumModeCommand(GroupCommandListener listener, Command paramCommand, BluetoothMode bluetoothMode, boolean isAutoRun) {
        super(paramCommand, bluetoothMode,isAutoRun);
        this.groupCommandListener = listener;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void execute(BluetoothGatt paramBluetoothGatt) {
        byte[] byteArray;
        byte[] b = bluetoothMode.byteArrayMessage;
        if(b.length == 4){
            byteArray = generatePasswordArray(b);
        }else{
            byteArray = generateEncryptedToken(b);
        }
        int dataLen = byteArray.length;
        byte[] dataArray = new byte[(dataLen + 0x1)];
        dataArray[0x0] = 0x11;
        System.arraycopy(byteArray, 0x0, dataArray, 0x1, dataLen);
        MessaagePacket icdMessageIn = new MessaagePacket();
        MessaagePacket icdMessageOut = new MessaagePacket(dataArray);
        insertCommand(new ReadMessagePacket(icdMessageIn,groupCommandListener,new HandlePremModeReply(groupCommandListener,null,bluetoothMode,icdMessageIn,true),bluetoothMode,false));
        for(int i = (icdMessageOut.getRequiredPacketCount() - 0x1); i >= 0; i--){
            insertCommand(new WriteData(groupCommandListener,new ReadMessagePacket(icdMessageIn,groupCommandListener,null,bluetoothMode,false),icdMessageOut.createPacketByteArray(i),bluetoothMode,true));
        }
        groupCommandListener.moveNextCommand(this);
    }

    private byte[] generateEncryptedToken(byte[] paramArrayOfByte)
    {
        byte[] arrayOfByte1 = { 72, 59, -45, -62, -53, -33, 99, 69, 22, 0, 4, -26, -43, 109, -108, -116 };
        byte[] arrayOfByte2 = new byte[16];
        byte[] arrayOfByte3 = BleMeterSerialData.reverseByteArray(paramArrayOfByte);
        System.arraycopy(arrayOfByte3, 2, arrayOfByte2, 0, 2);
        System.arraycopy(arrayOfByte3, 4, arrayOfByte2, 2, 4);
        System.arraycopy(arrayOfByte3, 0, arrayOfByte2, 6, 2);
        System.arraycopy(arrayOfByte2, 0, arrayOfByte2, 8, 8);
        try
        {
            Cipher localCipher = Cipher.getInstance("AES/ECB/NoPadding");
            localCipher.init(1, new SecretKeySpec(arrayOfByte1, "AES"));
            byte[] arrayOfByte4 = localCipher.doFinal(arrayOfByte2);
            return arrayOfByte4;
        }
        catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
        {
            //RLog.e(PremiumModeCommand.TAG, "" + localNoSuchAlgorithmException.getLocalizedMessage());
            //this.mListener.onSyncFailed();
            return paramArrayOfByte;
        }
        catch (NoSuchPaddingException localNoSuchPaddingException)
        {
            for (;;)
            {
                //RLog.e(PremiumModeCommand.TAG, "" + localNoSuchPaddingException.getLocalizedMessage());
                //this.mListener.onSyncFailed();
                return null;
            }
        }
        catch (IllegalBlockSizeException localIllegalBlockSizeException)
        {
            for (;;)
            {
                //RLog.e(PremiumModeCommand.TAG, "" + localIllegalBlockSizeException.getLocalizedMessage());
                //this.mListener.onSyncFailed();
                return null;
            }
        }
        catch (BadPaddingException localBadPaddingException)
        {
            for (;;)
            {
                //RLog.e(PremiumModeCommand.TAG, "" + localBadPaddingException.getLocalizedMessage());
                //this.mListener.onSyncFailed();
                return null;
            }
        }
        catch (InvalidKeyException localInvalidKeyException)
        {
            for (;;)
            {
                //RLog.e(PremiumModeCommand.TAG, "" + localInvalidKeyException.getLocalizedMessage());
                //this.mListener.onSyncFailed();
                return null;
            }
        }
    }

    private byte[] generatePasswordArray(byte[] paramArrayOfByte)
    {
        byte[] arrayOfByte1 = new byte[4];
        arrayOfByte1[0] = paramArrayOfByte[3];
        arrayOfByte1[1] = paramArrayOfByte[2];
        arrayOfByte1[2] = paramArrayOfByte[1];
        arrayOfByte1[3] = paramArrayOfByte[0];
        byte[] arrayOfByte2 = new byte[4];
        arrayOfByte2[0] = arrayOfByte1[2];
        arrayOfByte2[1] = arrayOfByte1[1];
        arrayOfByte2[2] = arrayOfByte1[0];
        arrayOfByte2[3] = arrayOfByte1[3];
        byte[] arrayOfByte3 = { -84, -50, 85, -19 };
        byte[] arrayOfByte4 = new byte[4];
        for (int i = 0; i < arrayOfByte2.length; i++) {
            arrayOfByte4[i] = ((byte)(arrayOfByte2[i] ^ arrayOfByte3[i]));
        }
        byte[] arrayOfByte5 = new byte[4];
        arrayOfByte5[0] = arrayOfByte4[3];
        arrayOfByte5[1] = arrayOfByte4[2];
        arrayOfByte5[2] = arrayOfByte4[1];
        arrayOfByte5[3] = arrayOfByte4[0];
        return arrayOfByte5;
    }

    static class HandlePremModeReply extends Command{
        MessaagePacket messaagePacket ;
        private GroupCommandListener listener;
        public HandlePremModeReply(GroupCommandListener listener,Command paramCommand, BluetoothMode bluetoothMode,MessaagePacket messaagePacket,boolean isAutoRun) {
            super(paramCommand, bluetoothMode,isAutoRun);
            this.messaagePacket = messaagePacket;
            this.listener = listener;
        }

        @Override
        public void execute(BluetoothGatt paramBluetoothGatt) {

            byte[] data = ProcessMeterDieID.parse(messaagePacket.getMessageBytes());//这里可能会抛异常
            listener.moveNextCommand(this);
        }
    }

}
