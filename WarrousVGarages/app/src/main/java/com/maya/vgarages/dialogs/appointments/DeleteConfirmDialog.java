package com.maya.vgarages.dialogs.appointments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.maya.vgarages.R;
import com.maya.vgarages.interfaces.dialog.IDeleteConfirmDialog;
import com.maya.vgarages.models.Appointment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gokul Kalagara on 5/24/2018.
 */
public class DeleteConfirmDialog extends Dialog
{
    @BindView(R.id.tvDetails)
    TextView tvDetails;

    @BindView(R.id.tvYes)
    TextView tvYes;

    @BindView(R.id.tvNo)
    TextView tvNo;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    Context context;
    Appointment appointment;
    IDeleteConfirmDialog iDeleteConfirmDialog;

    public DeleteConfirmDialog(@NonNull Context context, Appointment appointment, IDeleteConfirmDialog iDeleteConfirmDialog) {
        super(context);
        this.context = context;
        this.appointment = appointment;
        this.iDeleteConfirmDialog = iDeleteConfirmDialog;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.replace_cart_dialog);
        ButterKnife.bind(this);

        initialize();
    }

    private void initialize()
    {
        tvTitle.setText("Confirm Cancel?");
        tvDetails.setText("Do you want to cancel this appointment?");
        tvYes.setOnClickListener(v -> {
            this.dismiss();
            iDeleteConfirmDialog.deleteAppointment(appointment);
        });
        tvNo.setOnClickListener(v ->
        {
            this.dismiss();
        });
    }
}
