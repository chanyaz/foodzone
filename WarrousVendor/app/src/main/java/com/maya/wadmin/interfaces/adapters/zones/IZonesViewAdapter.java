package com.maya.wadmin.interfaces.adapters.zones;

import android.view.View;

import com.maya.wadmin.fragments.lot.violations.ViolationVehiclesFragment;
import com.maya.wadmin.models.Zone;

/**
 * Created by Gokul Kalagara on 2/22/2018.
 */

public interface IZonesViewAdapter
{
    public void editZone(Zone zone,int position);
    public void openPopUp(View view, Zone zone, int position);
    public void deleteZone(Zone zone,int position);
}
