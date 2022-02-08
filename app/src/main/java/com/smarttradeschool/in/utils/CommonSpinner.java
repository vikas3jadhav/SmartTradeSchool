package com.smarttradeschool.in.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.androidbuts.multispinnerfilter.ClassSectionData;
import com.androidbuts.multispinnerfilter.KeyPairBoolData;
import com.androidbuts.multispinnerfilter.MultiSpinnerSerachWithoutSection;
import com.androidbuts.multispinnerfilter.SpinnerListener;

import java.util.ArrayList;
import java.util.List;

public class CommonSpinner {

    Context context;

    public CommonSpinner(Context context) {
        this.context = context;
    }

    public void populateNewDropdownWithoutSection(List<String> busnolist,
                                                  MultiSpinnerSerachWithoutSection multiSpinnerSearch,
                                                  String mod, String selectedClasses,
                                                  List<String> shift_list,
                                                  List<Integer> shift_position_list, boolean
                                                          isPerformClick) {

        String[] array = selectedClasses.split("\\s*,\\s*");
        int selected = 0;

/*        for(int k = 0; k< array.length; k++){
            Toast.makeText(context, array[k],Toast.LENGTH_SHORT).show();
        }*/

        List<KeyPairBoolData> listArray0 = new ArrayList<>();
        List<ClassSectionData> listArray1 = new ArrayList<>();
        for (int i = 0; i < busnolist.size(); i++) {
            KeyPairBoolData h = new KeyPairBoolData();
            ClassSectionData classSectionData = new ClassSectionData();
            classSectionData.setShift(shift_list.get(i));
            h.setId(i + 1);
            h.setName(busnolist.get(i));
            listArray0.add(h);
            listArray1.add(classSectionData);

            if (mod.equals("edit")) {
                for (int j = 0; j < array.length; j++) {
                    if (busnolist.get(i).equals(array[j])) {
                        h.setSelected(true);
                        selected++;
                    }
                }
            }
        }

        if(selected == 0){
            selected = -1;
        }

        multiSpinnerSearch.setItems(listArray0, selected, new SpinnerListener() {
            @Override
            public void onItemsSelected(List<KeyPairBoolData> items) {
                for (int i = 0; i < items.size(); i++) {
                    if (items.get(i).isSelected()) {
                        Log.i("multiselect", i + " : " + items.get(i).getName() + " : " + items
                                .get(i).isSelected());
                    }
                }
            }
        }, listArray1, shift_position_list);

        if (isPerformClick) {
            multiSpinnerSearch.performClick();
        }
    }

    public List<String> getTradingSegment(final MultiSpinnerSerachWithoutSection
                                                    multiSpinnerSearch,
                                            final String mod, final String
                                                    selectedClasses,
                                            final boolean isPerformClick) {

        final List<String> busname_list = new ArrayList<String>();
        final List<String> busno_list = new ArrayList<String>();
        final List<Integer> shift_position_list = new ArrayList<>();


        String shiftgeneral = "";

        busno_list.add("Cash");
        busno_list.add("Future");
        busno_list.add("Stock Option");
        busno_list.add("Nifty BankNifty Option");

        busname_list.add("0");
        busname_list.add("1");
        busname_list.add("2");
        busname_list.add("3");

        shift_position_list.add(0);
        shift_position_list.add(1);
        shift_position_list.add(2);
        shift_position_list.add(3);


        populateNewDropdownWithoutSection(busno_list, multiSpinnerSearch, mod,
                selectedClasses, busname_list, shift_position_list, isPerformClick);

        return busname_list;
    }
}
