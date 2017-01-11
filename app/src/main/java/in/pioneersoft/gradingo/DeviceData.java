package in.pioneersoft.gradingo;

import com.jaredrummler.android.device.DeviceName;

import java.io.Serializable;

/**
 * Created by prsingh on 12/13/2016 AD.
 */

public class DeviceData implements Serializable {
    //Data Fields - Start
    private String manufacturer;
    private String marketName;
    private String codeName;
    private String model;
    private String fullName;
    private String imeiNumber1;
    private String imeiNumber2;
    private String wifiMACAddress;
    private String bluetoothMacAddress;
    private String softwareVersion;
    private String phoneType;
    private String simState;
    private String ramTotalSize;
    private String ramAvailableSize;
    private String internalMemoryTotal;
    private String internalMemoryAvailable;
    private String externalMemoryTotal;
    private String externalMemoryAvailable;
    //Data Fields - End


    public String getManufacturer(){
        return manufacturer;
    }
    public void setManufacturer(DeviceName.DeviceInfo info){
        manufacturer = info.manufacturer;
    }


    public String getMarketName(){
        return marketName;
    }
    public void setMarketName(DeviceName.DeviceInfo info){
        marketName=info.marketName;
    }

    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(DeviceName.DeviceInfo info) {
        this.codeName = info.codename;
    }

    public String getModel() {
        return model;
    }

    public void setModel(DeviceName.DeviceInfo info) {
        this.model = info.model;
    }

    public String getFullName() {
        return fullName;
    }
    public void setFullName(DeviceName.DeviceInfo info) {
        this.fullName = info.getName();
    }


    public String getImeiNumber1(){
        return imeiNumber1;
    }
    public void setImeiNumber1(String value){
        imeiNumber1=value;
    }

    public String getImeiNumber2(){
        return imeiNumber2;
    }
    public void setImeiNumber2(String value){
        imeiNumber2=value;
    }

    public String getWifiMACAddress(){
        return wifiMACAddress;
    }
    public void setWifiMACAddress(String value){
        wifiMACAddress=value;
    }

    public String getBluetoothMacAddress(){
        return bluetoothMacAddress;
    }
    public void setBluetoothMacAddress(String value){
        bluetoothMacAddress=value;
    }

    public String getSoftwareVersion(){
        return softwareVersion;
    }
    public void setSoftwareVersion(String value){
        softwareVersion=value;
    }

    public String getPhoneType(){
        return phoneType;
    }
    public void setPhoneType(String value){
        phoneType=value;
    }

    public String getSimState(){
        return simState;
    }
    public void setSimState(String value){
        simState=value;
    }

    public String getRamTotalSize(){
        return ramTotalSize;
    }
    public void setRamTotalSize(String value){
        ramTotalSize=value;
    }

    public String getRamAvailableSize(){
        return ramAvailableSize;
    }
    public void setRamAvailableSize(String value){
        ramAvailableSize=value;
    }

    public String getInternalMemoryTotal(){
        return internalMemoryTotal;
    }
    public void setInternalMemoryTotal(String value){
        internalMemoryTotal=value;
    }


    public String getInternalMemoryAvailable(){
        return internalMemoryAvailable;
    }
    public void setInternalMemoryAvailable(String value){
        internalMemoryAvailable=value;
    }

    public String getExternalMemoryTotal(){
        return externalMemoryTotal;
    }
    public void setExternalMemoryTotal(String value){
        externalMemoryTotal=value;
    }

    public String getExternalMemoryAvailable(){
        return externalMemoryAvailable;
    }
    public void setExternalMemoryAvailable(String value){
        externalMemoryAvailable=value;
    }
}
