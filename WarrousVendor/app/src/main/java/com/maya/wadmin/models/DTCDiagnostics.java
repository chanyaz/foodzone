package com.maya.wadmin.models;

import java.io.Serializable;

/**
 * Created by Gokul Kalagara on 1/31/2018.
 */

public class DTCDiagnostics implements Serializable
{
    public int DtcCodeStatusId;
    public int VehicleId;
    public int DtcCodeId;
    public String Code;
    public String CodeDesc;
    public boolean Status;
    public String Description;
}
