package com.androidbuts.multispinnerfilter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

//import android.app.AlertDialog;

public class MultiSelectSearch1 extends AppCompatSpinner implements OnCancelListener {
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

    public MultiSelectSearch1(Context context) {
        super(context);
    }

    public MultiSelectSearch1(Context arg0, AttributeSet arg1) {
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

    public MultiSelectSearch1(Context arg0, AttributeSet arg1, int arg2) {
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
        adapter = new MyAdapter(getContext(), items, classSectionDataList);
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

        selectAllCheckbox.setOnClickListener(new OnClickListener() {
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
        if (spinnerBuffer.length() > 2)
            defaultText = spinnerBuffer.toString().substring(0, spinnerBuffer.toString().length() - 2);

        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(getContext(), R.layout.textview_for_spinner, new String[]{defaultText});
        setAdapter(adapterSpinner);

        if (position != -1) {
            items.get(position).setSelected(true);
            //listener.onItemsSelected(items);
            onCancel(null);
        }
    }

    public interface LimitExceedListener {
        void onLimitListener(KeyPairBoolData data);
    }

    //Adapter Class
    public class MyAdapter extends BaseAdapter implements Filterable {

        private static final int TYPE_ITEM = 0;
        private static final int TYPE_SEPARATOR = 1;

        //String sectionName;

        List<ClassSectionData> classSectionDataList;
        List<KeyPairBoolData> arrayList;
        List<KeyPairBoolData> mOriginalValues; // Original Values
        LayoutInflater inflater;

        public MyAdapter(Context context, List<KeyPairBoolData> arrayList, List<ClassSectionData> classSectionDataList) {
            this.classSectionDataList = classSectionDataList;
            this.arrayList = arrayList;
            //inflater = LayoutInflater.from(context);
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

        @Override
        public int getItemViewType(int position) {
            //return sectionHeader.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM;
            /*if(position == 0){
                return TYPE_SEPARATOR;
            }
            else if(position == 5 || position == 10 || position == 15 || position == 20 || position == 25){
                return TYPE_SEPARATOR;
            }
            else {
                return TYPE_ITEM;
            }*/
            if(shift_position_list.contains(position)){
                return TYPE_SEPARATOR;
            }
            else{
                return TYPE_ITEM;
            }
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
                //r = 1;
                return TYPE_SEPARATOR;
            }
            else if(position != 0){
                int rr = 0;
                if(sectionName.equals(classSectionDataList.get(position).getShift())){
                    *//*holder.sectiontext.setVisibility(GONE);
                    holder.shiftView.setVisibility(GONE);*//*
                    sectionName = classSectionDataList.get(position).getShift();
                    //r = 0;
                    rr = TYPE_ITEM;
                    //return 0;
                    //notifyDataSetChanged();
                }
                else if(!sectionName.equals(classSectionDataList.get(position).getShift())){
                    *//*holder.sectiontext.setVisibility(VISIBLE);
                    holder.shiftView.setVisibility(VISIBLE);
                    holder.sectiontext.setText(classSectionDataList.get(position).getShift());*//*
                    sectionName = classSectionDataList.get(position).getShift();
                    //r = 1;
                    rr = TYPE_SEPARATOR;
                    //return 1;
                    //notifyDataSetChanged();
                }
                return rr;
            }
            else{
                return TYPE_ITEM;
            }
        }*/

        @Override
        public int getViewTypeCount() {
            return 2; // Count of different layouts
        }

        private class ViewHolder {
            TextView textView;
            CheckBox checkBox;
            TextView sectiontext;
            CheckBox checkboxSelectAllShift;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            int rowType = getItemViewType(position);

            if (convertView == null) {
                holder = new ViewHolder();
                switch (rowType) {
                    case TYPE_ITEM:
                        convertView = inflater.inflate(R.layout.item_listview_multiple_only, null);
                        holder.textView = (TextView) convertView.findViewById(R.id.alertTextView);
                        holder.checkBox = (CheckBox) convertView.findViewById(R.id.alertCheckbox);
                        holder.sectiontext = (TextView) convertView.findViewById(R.id.textSeparator);
                        holder.checkboxSelectAllShift = (CheckBox) convertView.findViewById(R.id.checkboxSelectAllShift);
                        break;
                    case TYPE_SEPARATOR:
                        convertView = inflater.inflate(R.layout.shift_selection_only_single_view, null);
                        holder.textView = (TextView) convertView.findViewById(R.id.alertTextView);
                        holder.checkBox = (CheckBox) convertView.findViewById(R.id.alertCheckbox);
                        holder.sectiontext = (TextView) convertView.findViewById(R.id.textSeparator);
                        holder.checkboxSelectAllShift = (CheckBox) convertView.findViewById(R.id.checkboxSelectAllShift);
                        break;
                }
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            //final KeyPairBoolData data = arrayList.get(position);
            //holder.textView.setText(data.getName());

            holder.sectiontext.setText(classSectionDataList.get(position).getShift());
            final int backgroundColor = R.color.list_odd;
            convertView.setBackgroundColor(ContextCompat.getColor(getContext(), backgroundColor));

            final KeyPairBoolData data = arrayList.get(position);
            holder.textView.setText(data.getName());
            holder.textView.setTypeface(null, Typeface.NORMAL);
            holder.checkBox.setChecked(data.isSelected());

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
            if (data.isSelected()) {
                holder.textView.setTypeface(null, Typeface.BOLD);
                holder.textView.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                convertView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.selected));
            }
            else{
                holder.textView.setTextColor(ContextCompat.getColor(getContext(), R.color.text_color));
            }
            holder.checkBox.setTag(holder);

/*            if (type == 1) {
                holder.sectiontext.setText(classSectionDataList.get(position).getShift());
            }
            else {
                holder.textView = (TextView) convertView.findViewById(R.id.alertTextView);
                holder.checkBox = (CheckBox) convertView.findViewById(R.id.alertCheckbox);
                final int backgroundColor = R.color.list_odd;
                convertView.setBackgroundColor(ContextCompat.getColor(getContext(), backgroundColor));

                final KeyPairBoolData data = arrayList.get(position);
                holder.textView.setText(data.getName());
                holder.textView.setTypeface(null, Typeface.NORMAL);
                holder.checkBox.setChecked(data.isSelected());

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
                if (data.isSelected()) {
                    holder.textView.setTypeface(null, Typeface.BOLD);
                    holder.textView.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                    convertView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.selected));
                }
                else{
                    holder.textView.setTextColor(ContextCompat.getColor(getContext(), R.color.text_color));
                }
                holder.checkBox.setTag(holder);
            }*/

            return convertView;
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
                        constraint = constraint.toString().toLowerCase();
                        for (int i = 0; i < mOriginalValues.size(); i++) {
                            Log.i(TAG, "Filter : " + mOriginalValues.get(i).getName() + " -> " + mOriginalValues.get(i).isSelected());
                            String data = mOriginalValues.get(i).getName();
                            if (data.toLowerCase().contains(constraint.toString())) {
                                FilteredArrList.add(mOriginalValues.get(i));
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