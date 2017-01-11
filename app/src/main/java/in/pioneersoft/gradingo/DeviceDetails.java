package in.pioneersoft.gradingo;

import android.os.Bundle;
import android.app.Activity;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class DeviceDetails extends Activity {

    DeviceData data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_details);
        Bundle b = this.getIntent().getExtras();
        if (b != null)
            data = (DeviceData) b.getSerializable("DeviceData");
        TextView viewManufacturer = (TextView) findViewById(R.id.manufacturer);
        viewManufacturer.setText(data.getManufacturer());
        TextView viewMarketName = (TextView)findViewById(R.id.marketName);
        viewMarketName.setText(data.getMarketName());
        TextView viewCodeName = (TextView)findViewById(R.id.codeName);
        viewCodeName.setText(data.getMarketName());
        TextView viewModel = (TextView)findViewById(R.id.model);
        viewModel.setText(data.getModel());
        TextView viewFullName = (TextView)findViewById(R.id.fullName);
        viewFullName.setText(data.getFullName());
        TextView viewImei1 = (TextView)findViewById(R.id.imei1);
        viewImei1.setText(data.getImeiNumber1());
        TextView viewImei2 = (TextView)findViewById(R.id.imei2);
        viewImei2.setText(data.getImeiNumber2());
        TextView viewWiFiAddress = (TextView)findViewById(R.id.wifiMacAddress);
        viewWiFiAddress.setText(data.getWifiMACAddress());
        TextView viewBTAddress = (TextView)findViewById(R.id.btMacAddress);
        viewBTAddress.setText(data.getBluetoothMacAddress());
        TextView viewSWVersion = (TextView)findViewById(R.id.swVersion);
        viewSWVersion.setText(data.getSoftwareVersion());
        TextView viewPhoneType = (TextView)findViewById(R.id.phoneType);
        viewPhoneType.setText(data.getPhoneType());
        TextView viewSimState = (TextView)findViewById(R.id.simState);
        viewSimState.setText(data.getSimState());
        TextView viewTotalRam = (TextView)findViewById(R.id.totalRAM);
        viewTotalRam.setText(data.getRamTotalSize());
        TextView viewAvailableRam = (TextView)findViewById(R.id.availableRAM);
        viewAvailableRam.setText(data.getRamAvailableSize());
        TextView viewTotalInternalMemory = (TextView)findViewById(R.id.totalInternalMemory);
        viewTotalInternalMemory.setText(data.getInternalMemoryTotal());
        TextView viewAvailableInternalMemory = (TextView)findViewById(R.id.availableInternalMemory);
        viewAvailableInternalMemory.setText(data.getInternalMemoryAvailable());
        TextView viewTotalExternalMemory = (TextView)findViewById(R.id.totalExternalMemory);
        viewTotalExternalMemory.setText(data.getExternalMemoryTotal());
        TextView viewAvailableExternalMemory = (TextView)findViewById(R.id.availableExternalMemory);
        viewAvailableExternalMemory.setText(data.getExternalMemoryAvailable());
        PieChart viewInternalMemoryChart = (PieChart)findViewById(R.id.totalIMemoryChart);
        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
        Integer available = Integer.parseInt(data.getInternalMemoryAvailable().split(" ")[0]);
        Integer used = Integer.parseInt(data.getInternalMemoryTotal().split(" ")[0]) - available;
        entries.add(new PieEntry(used, "Used"));
        entries.add(new PieEntry(available, "Available"));
        PieDataSet dataSet = new PieDataSet(entries, "Internal Memory");
        ArrayList<Integer> colors = new ArrayList<Integer>();
        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);



        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);
        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        viewInternalMemoryChart.setHoleColor(ColorTemplate.JOYFUL_COLORS[4]);
        Description description = new Description();
        description.setText("Device Internal Memory");
        viewInternalMemoryChart.setDescription(description);
        viewInternalMemoryChart.setDrawEntryLabels(false);
        viewInternalMemoryChart.setData(new PieData(dataSet));
    }
}
