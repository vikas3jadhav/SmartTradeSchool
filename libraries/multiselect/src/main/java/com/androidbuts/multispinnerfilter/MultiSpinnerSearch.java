package com.androidbuts.multispinnerfilter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.AppCompatSpinner;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MultiSpinnerSearch extends AppCompatSpinner implements OnCancelListener {
    private static final String TAG = MultiSpinnerSearch.class.getSimpleName();
    private List<KeyPairBoolData> items;
    private List<ClassSectionData> classSectionDataList;
    private List<Integer> shift_position_list;
    private String defaultText = "";
    private String spinnerTitle = "";
    private SpinnerListener listener;
    private int limit = -1;
    private int selected = 0;
    private LimitExceedListener limitListener;
    MyAdapter adapter;
    public static AlertDialog.Builder builder;
    public static AlertDialog ad;

    public MultiSpinnerSearch(Context context) {
        super(context);
    }

    public MultiSpinnerSearch(Context arg0, AttributeSet arg1) {
        super(arg0, arg1);
        TypedArray a = arg0.obtainStyledAttributes(arg1, R.styleable.MultiSpinnerSearch);
        for (int i = 0; i < a.getIndexCount(); ++i) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.MultiSpinnerSearch_hintText) {
                spinnerTitle = a.getString(attr);
                defaultText = spinnerTitle;
                break;
            }
        }
        Log.i(TAG, "spinnerTitle: "+spinnerTitle);
        a.recycle();
    }

    public MultiSpinnerSearch(Context arg0, AttributeSet arg1, int arg2) {
        super(arg0, arg1, arg2);
    }

    public void setLimit(int limit, LimitExceedListener listener) {
        this.limit = limit;
        this.limitListener = listener;
    }

    public List<KeyPairBoolData> getSelectedItems() {
        List<KeyPairBoolData> selectedItems = new ArrayList<>();
        for(KeyPairBoolData item : items){
            if(item.isSelected()){
                selectedItems.add(item);
            }
        }
        return selectedItems;
    }

    public List<Long> getSelectedIds() {
        List<Long> selectedItemsIds = new ArrayList<>();
        for(KeyPairBoolData item : items){
            if(item.isSelected()){
                selectedItemsIds.add(item.getId());
            }
        }
        return selectedItemsIds;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        // refresh text on spinner

        StringBuilder spinnerBuffer = new StringBuilder();

        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).isSelected()) {
                spinnerBuffer.append(items.get(i).getName());
                spinnerBuffer.append(", ");
            }
        }

        String spinnerText = spinnerBuffer.toString();
        if (spinnerText.length() > 2)
            spinnerText = spinnerText.substring(0, spinnerText.length() - 2);
        else if(spinnerText.isEmpty())
            spinnerText = spinnerTitle;
        else
            spinnerText = defaultText;

        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(getContext(), R.layout.textview_for_spinner, new String[]{spinnerText});
        setAdapter(adapterSpinner);

        if (adapter != null)
            adapter.notifyDataSetChanged();

        listener.onItemsSelected(items);
    }

    @Override
    public boolean performClick() {
        if(items == null){
            Toast.makeText(getContext(),"Drop-down is Empty.",Toast.LENGTH_SHORT).show();
            return true;
        }

        //builder = new AlertDialog.Builder(getContext(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        builder = new AlertDialog.Builder(getContext(), R.style.myDialog);
        builder.setTitle(spinnerTitle);

        final LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View view = inflater.inflate(R.layout.alert_dialog_listview_search, null);
        builder.setView(view);

        final ListView listView = (ListView) view.findViewById(R.id.alertSearchListView);
        final CheckBox selectAllCheckbox = (CheckBox) view.findViewById(R.id.checkboxSelectAll);

        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setFastScrollEnabled(false);
        adapter = new MyAdapter(getContext(), items, classSectionDataList, listView);
        listView.setAdapter(adapter);

        final TextView emptyText = (TextView) view.findViewById(R.id.empty);
        listView.setEmptyView(emptyText);

        final EditText editText = (EditText) view.findViewById(R.id.alertSearchEditText);
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i(TAG, " ITEMS : " + items.size());
                dialog.cancel();
            }
        });

/*        selectAllCheckbox.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                List<KeyPairBoolData> allclasses=  ((MyAdapter) adapter).getAllClassList();

                if(selectAllCheckbox.isChecked()){
                    for ( int i=0; i< allclasses.size(); i++ ) {
                        KeyPairBoolData singleClass= (KeyPairBoolData) allclasses.get(i);
                        singleClass.setSelected(true);
                        adapter.notifyDataSetChanged();
                    }
                    Toast.makeText(getContext(),"All classes checked", Toast.LENGTH_SHORT).show();
                }
                else{
                    for ( int i=0; i< allclasses.size(); i++ ) {
                        KeyPairBoolData sigleClass= (KeyPairBoolData) allclasses.get(i);
                        sigleClass.setSelected(false);
                        adapter.notifyDataSetChanged();
                    }
                    Toast.makeText(getContext(),"All classes unckecked", Toast.LENGTH_SHORT).show();
                }
            }
        });*/

        selectAllCheckbox.setChecked(adapter.checkAllMarked());

        selectAllCheckbox.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                //List<KeyPairBoolData> allclassess=  ((MyAdapter) adapter).getAllClassList();

                Toast.makeText(getContext(),"Processing, Please Wait ...", Toast.LENGTH_LONG).show();
                ProgressDialog progressDialog = new ProgressDialog(getContext());
                progressDialog.setCancelable(false);
                progressDialog.show();
                if(selectAllCheckbox.isChecked()){
                    for ( int i=0; i< adapter.getCount(); i++ ) {
                        /*KeyPairBoolData singleClass= (KeyPairBoolData) allclasses.get(i);
                        singleClass.setSelected(true);
                        adapter.notifyDataSetChanged();*/
                        adapter.markChecked(i);

                    }
                    Toast.makeText(getContext(),"All classes checked", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
                else{
                    for ( int i=0; i< adapter.getCount(); i++ ) {
                        /*KeyPairBoolData sigleClass= (KeyPairBoolData) allclasses.get(i);
                        sigleClass.setSelected(false);
                        adapter.notifyDataSetChanged();*/
                        adapter.markUnChecked(i);
                    }
                    progressDialog.dismiss();
                    Toast.makeText(getContext(),"All classes unchecked", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setOnCancelListener(this);
        ad = builder.show();
        return true;
    }

    public void setItems(List<KeyPairBoolData> items, int position, SpinnerListener listener,
                         List<ClassSectionData> classSectionDataList, List<Integer> shift_position_list) {

        this.classSectionDataList = classSectionDataList;
        this.shift_position_list = shift_position_list;
        this.items = items;
        this.listener = listener;

        StringBuilder spinnerBuffer = new StringBuilder();

        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).isSelected()) {
                spinnerBuffer.append(items.get(i).getName());
                spinnerBuffer.append(", ");
            }
        }
        if (spinnerBuffer.length() > 2) {
            defaultText = spinnerBuffer.toString().substring(0, spinnerBuffer.toString().length() - 2);
        }
        else {
            //defaultText = "Select";
            defaultText = spinnerTitle;
        }

        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(getContext(), R.layout.textview_for_spinner, new String[]{defaultText});
        setAdapter(adapterSpinner);

        if (position != -1) {
            selected = position;
            //defaultText = "Select";
            //items.get(position).setSelected(true);
            //listener.onItemsSelected(items);
            //onCancel(null);
        }
    }

    public interface LimitExceedListener {
        void onLimitListener(KeyPairBoolData data);
    }

    //Adapter Class
    public class MyAdapter extends BaseAdapter implements Filterable {

        //String sectionName;
        int r;

        List<ClassSectionData> classSectionDataList;
        List<KeyPairBoolData> arrayList;
        List<KeyPairBoolData> mOriginalValues; // Original Values
        LayoutInflater inflater;
        ListView listView;

        public MyAdapter(Context context, List<KeyPairBoolData> arrayList, List<ClassSectionData> classSectionDataList, ListView listView) {
            this.classSectionDataList = classSectionDataList;
            this.arrayList = arrayList;
            inflater = LayoutInflater.from(context);
            this.listView = listView;
            //inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //sectionName = classSectionDataList.get(0).getShift();
        }

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

/*        @Override
        public int getItemViewType(int position) {
            // Define a way to determine which layout to use, here it's just evens and odds.
            //return position % 2;

            if(position == 0){
                //section code
                *//*holder.sectiontext.setVisibility(VISIBLE);
                holder.shiftView.setVisibility(VISIBLE);
                holder.sectiontext.setText(classSectionDataList.get(position).getShift());*//*
                r = 1;
            }
            else if(position != 0){
                if(sectionName.equals(classSectionDataList.get(position).getShift())){
                    *//*holder.sectiontext.setVisibility(GONE);
                    holder.shiftView.setVisibility(GONE);*//*
                    sectionName = classSectionDataList.get(position).getShift();
                    r = 0;
                    //notifyDataSetChanged();
                }
                else if(!sectionName.equals(classSectionDataList.get(position).getShift())){
                    *//*holder.sectiontext.setVisibility(VISIBLE);
                    holder.shiftView.setVisibility(VISIBLE);
                    holder.sectiontext.setText(classSectionDataList.get(position).getShift());*//*
                    sectionName = classSectionDataList.get(position).getShift();
                    r = 1;
                    //notifyDataSetChanged();
                }
            }
            return r;
        }

        @Override
        public int getViewTypeCount() {
            return 2; // Count of different layouts
        }*/

        private class ViewHolder {
            TextView textView;
            TextView sectiontext;
            CheckBox checkBox;
            RelativeLayout shiftView;
            CheckBox checkboxSelectAllShift;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            Log.i(TAG, "getView() enter");
            final ViewHolder holder;

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_listview_multiple,  parent, false);

                holder = new ViewHolder();

                /*// You can move this line into your constructor, the inflater service won't change.
                mInflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);*/
/*                if(getItemViewType(position) == 1)
                    convertView = inflater.inflate(R.layout.shift_section_single_view, parent, false);
                else
                    convertView = inflater.inflate(R.layout.item_listview_multiple, parent, false);
                // etc, etc...*/

                holder.textView = (TextView) convertView.findViewById(R.id.alertTextView);
                holder.checkBox = (CheckBox) convertView.findViewById(R.id.alertCheckbox);

                holder.shiftView = (RelativeLayout) convertView.findViewById(R.id.shiftView);
                holder.sectiontext = (TextView) convertView.findViewById(R.id.textSeparator);
                holder.checkboxSelectAllShift = (CheckBox) convertView.findViewById(R.id.checkboxSelectAllShift);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            //final int backgroundColor = (position%2 == 0) ? R.color.list_even : R.color.list_odd;
/*            final int backgroundColor = R.color.list_odd;
            convertView.setBackgroundColor(ContextCompat.getColor(getContext(), backgroundColor));*/

            final KeyPairBoolData data = arrayList.get(position);
            final ClassSectionData classdata = classSectionDataList.get(position);

            holder.sectiontext.setText(classSectionDataList.get(position).getShift());

/*            if(position == 0){
                //section code
                holder.sectiontext.setVisibility(VISIBLE);
                holder.shiftView.setVisibility(VISIBLE);
                holder.sectiontext.setText(classSectionDataList.get(position).getShift());
                //notifyDataSetChanged();
            }
            else {
                if(sectionName.equals(classSectionDataList.get(position).getShift())){
                    holder.sectiontext.setVisibility(GONE);
                    holder.shiftView.setVisibility(GONE);
                    sectionName = classSectionDataList.get(position).getShift();
                    //notifyDataSetChanged();
                }
                else if(!sectionName.equals(classSectionDataList.get(position).getShift())){
                    holder.sectiontext.setVisibility(VISIBLE);
                    holder.shiftView.setVisibility(VISIBLE);
                    holder.sectiontext.setText(classSectionDataList.get(position).getShift());
                    sectionName = classSectionDataList.get(position).getShift();
                    //notifyDataSetChanged();
                }
            }*/

            if(shift_position_list.contains(position)){
                holder.sectiontext.setVisibility(VISIBLE);
                holder.shiftView.setVisibility(VISIBLE);
                holder.sectiontext.setText(classSectionDataList.get(position).getShift());
            }
            else{
                holder.sectiontext.setVisibility(GONE);
                holder.shiftView.setVisibility(GONE);
            }

            holder.textView.setText(data.getName());
            holder.textView.setTypeface(null, Typeface.NORMAL);
            holder.checkBox.setChecked(data.isSelected());
            holder.checkboxSelectAllShift.setChecked(classdata.isSelected());

/*            final List<KeyPairBoolData> allclasses=  ((MyAdapter) adapter).getAllClassList();
            holder.checkboxSelectAllShift.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(holder.checkboxSelectAllShift.isChecked()){
                        for(int i = 0; i<getCount(); i++){
                            String shiftnameforslectall = classSectionDataList.get(i).getShift();
                            if(shiftnameforslectall.equals(classSectionDataList.get(position).getShift())) {
                                holder.checkBox.setChecked(true);
                                KeyPairBoolData singleClass= (KeyPairBoolData) allclasses.get(i);
                                singleClass.setSelected(true);
                                adapter.notifyDataSetChanged();
                            }
                        }
                        Toast.makeText(getContext(),"All"+classSectionDataList.get(position).getShift()+"classes checked", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        for(int i = 0; i<getCount(); i++){
                            String shiftnameforslectall = classSectionDataList.get(i).getShift();
                            if(shiftnameforslectall.equals(classSectionDataList.get(position).getShift())) {
                                holder.checkBox.setChecked(false);
                                KeyPairBoolData singleClass= (KeyPairBoolData) allclasses.get(i);
                                singleClass.setSelected(false);
                                adapter.notifyDataSetChanged();
                            }
                        }
                        Toast.makeText(getContext(),"All"+classSectionDataList.get(position).getShift()+"classes unchecked", Toast.LENGTH_SHORT).show();
                    }
                }
            });*/

/*            holder.checkboxSelectAllShift.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(holder.checkboxSelectAllShift.isChecked()){
                        for(int i = position; i<getCount(); i++){
                            String shiftnameforslectall = classSectionDataList.get(i).getShift();
                            if(shiftnameforslectall.equals(classSectionDataList.get(position).getShift())) {
                                final KeyPairBoolData data = arrayList.get(i);
                                holder.checkBox.setChecked(true);
                                data.setSelected(true);
                                Log.i(TAG, "On Click Selected Item : " + data.getName() + " : " + data.isSelected());
                                notifyDataSetChanged();
                            }
                        }
                        Toast.makeText(getContext(),"All "+classSectionDataList.get(position).getShift()+" classes checked", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        for(int i = position; i<getCount(); i++){
                            String shiftnameforslectall = classSectionDataList.get(i).getShift();
                            if(shiftnameforslectall.equals(classSectionDataList.get(position).getShift())) {
                                final KeyPairBoolData data = arrayList.get(i);
                                holder.checkBox.setChecked(false);
                                data.setSelected(false);
                                Log.i(TAG, "On Click Selected Item : " + data.getName() + " : " + data.isSelected());
                                notifyDataSetChanged();
                            }
                        }
                        Toast.makeText(getContext(),"All"+classSectionDataList.get(position).getShift()+"classes unchecked", Toast.LENGTH_SHORT).show();
                    }
                }
            });*/

            holder.checkboxSelectAllShift.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(holder.checkboxSelectAllShift.isChecked()){
                        for(int i = position; i<getCount(); i++){
                            String shiftnameforslectall = classSectionDataList.get(i).getShift();
                            if(shiftnameforslectall.equals(classSectionDataList.get(position).getShift())) {
                                // final KeyPairBoolData data = arrayList.get(i);
                                //holder.checkBox.setChecked(true);
                                //data.setSelected(true);
                                //Log.i(TAG, "On Click Selected Item : " + data.getName() + " : " + data.isSelected());
                                // notifyDataSetChanged();

                                //final KeyPairBoolData data = arrayList.get(position);


                                final KeyPairBoolData data = arrayList.get(i);
                                if(data.isSelected()) { // deselect
                                    //selected--;
                                } else if(selected == limit) { // select with limit
                                    if(limitListener != null)
                                        limitListener.onLimitListener(data);
                                    return;
                                } else { // selected
                                    selected++;
                                    final ViewHolder temp = (ViewHolder) getViewByPosition(position,listView).getTag();
                                    temp.checkBox.setChecked(true);
                                    data.setSelected(true);
                                    Log.i(TAG, "On Click Selected Item : " + data.getName() + " : " + data.isSelected());
                                    notifyDataSetChanged();
                                }

                            }
                        }
                        holder.checkboxSelectAllShift.setChecked(true);
                        classdata.setSelected(true);
                        notifyDataSetChanged();
                        Toast.makeText(getContext(),"All "+classSectionDataList.get(position).getShift()+" checked", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        for(int i = position; i<getCount(); i++){
                            String shiftnameforslectall = classSectionDataList.get(i).getShift();
                            if(shiftnameforslectall.equals(classSectionDataList.get(position).getShift())) {
                                /*final KeyPairBoolData data = arrayList.get(i);
                                holder.checkBox.setChecked(false);
                                data.setSelected(false);
                                Log.i(TAG, "On Click Selected Item : " + data.getName() + " : " + data.isSelected());
                                notifyDataSetChanged();*/

                                //final KeyPairBoolData data = arrayList.get(position);
                                final KeyPairBoolData data = arrayList.get(i);
                                if(data.isSelected()) { // deselect
                                    selected--;
                                    final ViewHolder temp = (ViewHolder) getViewByPosition(position,listView).getTag();
                                    temp.checkBox.setChecked(false);
                                    data.setSelected(false);
                                    Log.i(TAG, "On Click Selected Item : " + data.getName() + " : " + data.isSelected());
                                    notifyDataSetChanged();
                                } else if(selected == limit) { // select with limit
                                    if(limitListener != null)
                                        limitListener.onLimitListener(data);
                                    return;
                                } else { // selected
                                    //selected++;
                                }
                            }
                        }

                        holder.checkboxSelectAllShift.setChecked(false);
                        classdata.setSelected(false);
                        notifyDataSetChanged();
                        Toast.makeText(getContext(),"All "+classSectionDataList.get(position).getShift()+" unchecked", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            convertView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if(data.isSelected()) { // deselect
                        selected--;
                    } else if(selected == limit) { // select with limit
                        if(limitListener != null)
                            limitListener.onLimitListener(data);
                        return;
                    } else { // selected
                        selected++;
                    }

                    final ViewHolder temp = (ViewHolder) v.getTag();
                    temp.checkBox.setChecked(!temp.checkBox.isChecked());

                    data.setSelected(!data.isSelected());
                    Log.i(TAG, "On Click Selected Item : " + data.getName() + " : " + data.isSelected());
                    notifyDataSetChanged();
                }
            });
/*            if (data.isSelected()) {
                holder.textView.setTypeface(null, Typeface.BOLD);
                holder.textView.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                convertView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.app_topbar_color));
            }
            else{
                holder.textView.setTextColor(ContextCompat.getColor(getContext(), R.color.text_color));
            }*/

/*            if (data.isSelected()) {
                //holder.textView.setTypeface(null, Typeface.BOLD);
                //holder.textView.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                //convertView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.app_topbar_color));
                //holder.textView.setTextColor(ContextCompat.getColor(getContext(), R.color.text_color));
            }
            else{
                //holder.textView.setTextColor(ContextCompat.getColor(getContext(), R.color.text_color));
            }*/

            holder.checkBox.setTag(holder);
            holder.checkboxSelectAllShift.setTag(holder);

            return convertView;
        }

        public boolean checkAllMarked(){
            int counter = 0;
            for(int i=0; i<adapter.getCount(); i++){

                final KeyPairBoolData data = arrayList.get(i);

                if(data.isSelected()) { // deselect
                    //selected--;
                    //b = true;
                    counter++;
                } else if(selected == limit) { // select with limit
                    if(limitListener != null)
                        limitListener.onLimitListener(data);
                    //return;
                    //b = false;
                } else { // selected
                    //selected++;
                    //b = false;
                    counter--;
                }


            }

            if(counter == adapter.getCount())
                return true;
            else return false;
        }

/*        public boolean checkAllShiftMarked(){
            int counter = 0;

            for(int i=0; i<adapter.getCount(); i++){
                final KeyPairBoolData data = arrayList.get(i);

                if(data.isSelected()) { // deselect
                    //selected--;
                    //b = true;
                    counter++;
                } else if(selected == limit) { // select with limit
                    if(limitListener != null)
                        limitListener.onLimitListener(data);
                    //return;
                    //b = false;
                } else { // selected
                    //selected++;
                    //b = false;
                    counter--;
                }
            }

            if(counter == adapter.getCount()) return true;
            else return false;
        }*/

        public void markChecked(int position){

            /*for(int i = 0; i<shift_position_list.size(); i++){
                final ClassSectionData shiftdata = classSectionDataList.get(position);
                final ViewHolder temp = (ViewHolder) getViewByPosition(position,listView).getTag();
                temp.checkboxSelectAllShift.setChecked(true);
                shiftdata.setSelected(true);
            }*/

            final ClassSectionData shiftdata = classSectionDataList.get(position);


            if(shiftdata.isSelected()) { // deselect
                //selected--;
            }  else { // selected
                //selected++;

                final ViewHolder temp2 = (ViewHolder) getViewByPosition(position,listView).getTag();
                temp2.checkboxSelectAllShift.setChecked(true);
                shiftdata.setSelected(true);
            }

            final KeyPairBoolData data = arrayList.get(position);

/*            if(data.isSelected()) { // deselect
                selected--;
            } else if(selected == limit) { // select with limit
                if(limitListener != null)
                    limitListener.onLimitListener(data);
                return;
            } else { // selected
                selected++;
            }*/

            if(data.isSelected()) { // deselect
                //selected--;
            } else if(selected == limit) { // select with limit
                if(limitListener != null)
                    limitListener.onLimitListener(data);
                return;
            } else { // selected
                //selected++;

                selected++;

                final ViewHolder temp = (ViewHolder) getViewByPosition(position,listView).getTag();
                temp.checkBox.setChecked(true);

                data.setSelected(true);
                Log.i(TAG, "On Click Selected Item : " + data.getName() + " : " + data.isSelected());
                notifyDataSetChanged();
            }
        }

        public void markUnChecked(int position){

            for(int i = 0; i<shift_position_list.size(); i++){
            }


            final ClassSectionData shiftdata = classSectionDataList.get(position);

            if(shiftdata.isSelected()) { // deselect
                //selected--;
                final ViewHolder temp2 = (ViewHolder) getViewByPosition(position,listView).getTag();
                temp2.checkboxSelectAllShift.setChecked(false);
                shiftdata.setSelected(false);
            }  else { // selected
                //selected++;
            }

            final KeyPairBoolData data = arrayList.get(position);

            if(data.isSelected()) { // deselect
                selected--;
                final ViewHolder temp = (ViewHolder) getViewByPosition(position,listView).getTag();
                temp.checkBox.setChecked(false);

                data.setSelected(false);
                Log.i(TAG, "On Click Selected Item : " + data.getName() + " : " + data.isSelected());
                notifyDataSetChanged();

            } else if(selected == limit) { // select with limit
                if(limitListener != null)
                    limitListener.onLimitListener(data);
                return;
            } else { // selected
                //selected++;
            }

            //selected--;

        }

        public View getViewByPosition(int pos, ListView listView) {
            final int firstListItemPosition = listView.getFirstVisiblePosition();
            final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

            if (pos < firstListItemPosition || pos > lastListItemPosition ) {
                return listView.getAdapter().getView(pos, null, listView);
            } else {
                final int childIndex = pos - firstListItemPosition;
                return listView.getChildAt(childIndex);
            }
        }

        // method to access in activity after updating selection
        public List<KeyPairBoolData> getAllClassList() {
            return arrayList;
        }

        @SuppressLint("DefaultLocale")
        @Override
        public Filter getFilter() {
            return new Filter() {

                @SuppressWarnings("unchecked")
                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {

                    arrayList = (List<KeyPairBoolData>) results.values; // has the filtered values
                    notifyDataSetChanged();  // notifies the data with new filtered values
                }

                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                    List<KeyPairBoolData> FilteredArrList = new ArrayList<>();

                    if (mOriginalValues == null) {
                        mOriginalValues = new ArrayList<>(arrayList); // saves the original data in mOriginalValues
                    }

                    /********
                     *
                     *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
                     *  else does the Filtering and returns FilteredArrList(Filtered)
                     *
                     ********/

                    if (constraint == null || constraint.length() == 0) {
                        // set the Original result to return
                        results.count = mOriginalValues.size();
                        results.values = mOriginalValues;
                    } else {
                        /*constraint = constraint.toString().toLowerCase();
                        for (int i = 0; i < mOriginalValues.size(); i++) {
                            Log.i(TAG, "Filter : " + mOriginalValues.get(i).getName() + " -> " + mOriginalValues.get(i).isSelected());
                            String data = mOriginalValues.get(i).getName();
                            if (data.toLowerCase().startsWith(constraint.toString())) {
                                FilteredArrList.add(mOriginalValues.get(i));
                            }
                        }*/

                        //constraint = constraint.toString().toLowerCase();
                        for (int i = 0; i < mOriginalValues.size(); i++) {
                            Log.i(TAG, "Filter : " + mOriginalValues.get(i).getName() + " -> " + mOriginalValues.get(i).isSelected());
                            String data = mOriginalValues.get(i).getName();

                            constraint = constraint.toString().toLowerCase();
                            String[] words = data.split("\\s+");
                            for (int j = 0; j < words.length; j++) {
                                words[j] = words[j].replaceAll("[^\\w]", "");
                                if (words[j].toLowerCase().startsWith(constraint.toString())) {
                                    FilteredArrList.add(mOriginalValues.get(i));
                                }
                            }
                        }
                        // set the Filtered result to return
                        results.count = FilteredArrList.size();
                        results.values = FilteredArrList;
                    }
                    return results;
                }
            };
        }
    }
}
