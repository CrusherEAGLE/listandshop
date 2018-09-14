package mygrocerylist.listandshop;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class RecyclerViewClass extends RecyclerView.Adapter<RecyclerViewClass.PersonViewHolder> {

    ArrayList<String> myList;
    ArrayList<Integer> amount;
    ArrayList<Integer> qty;
    Boolean[] theCalendar;

    ArrayList<Integer> pop;

    Context context;

    public RecyclerViewClass(Context context) {
        this.context = context;
    }


    public RecyclerViewClass(Context context, ArrayList<String> myList, ArrayList<Integer> amount) {
        this.myList = myList;
        this.amount = amount;
        this.context = context;

        theCalendar = MainActivity.loadArray(context);


        pop = new ArrayList<>();

        for (int i = 0; i <= 100; i++) {
            pop.add(i);
        }

    }

    @NonNull
    @Override
    public PersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(mygrocerylist.listandshop.R.layout.recycler, parent, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
        //inflates layout
    }

    @Override
    public void onBindViewHolder(@NonNull final PersonViewHolder holder, final int position) {
        holder.nameHolder.setText(myList.get(position));
        holder.icon.setImageResource(holder.imgs.getResourceId(amount.get(position), -1));



        if (!theCalendar[position]) {
            holder.calendar.setImageResource(mygrocerylist.listandshop.R.drawable.calendarblack);

        } else
            holder.calendar.setImageResource(mygrocerylist.listandshop.R.drawable.calendargrey);

/*         holder.spinner.setAdapter(holder.adapter);
        qty = MainActivity.getArrayListIntFirstTime("Qty", context);
        holder.spinner.setSelection(qty.get(position));

        holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                //((TextView) adapterView.getChildAt(0)).setTextSize(25);
                qty.set(position, Integer.valueOf(holder.spinner.getSelectedItem().toString()));
                MainActivity.saveArrayListInt(qty, "Qty", context);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });*/
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        //i have no idea
    }

    @Override
    public int getItemCount() {
        if (myList == null)
            return 0;
        return myList.size();

    }

    public void Remove(Context context, int position) {


        //qty.remove(position);
        myList.remove(position);
        amount.remove(position);
        theCalendar[position] = false;
        notifyItemRemoved(position);

        if (theCalendar[position] == true){
            cancelAlarm(position,context);
            theCalendar[position] = false; }

        //notifyItemRangeChanged(position, myList.size());
        MainActivity.saveArrayList(myList, "Names", context);
        MainActivity.saveArrayListInt(amount, "Image", context);
        //MainActivity.saveArrayListInt(qty, "Qty", context);
        MainActivity.storeArray(theCalendar, context); //calendar icon

    }


    public class PersonViewHolder extends RecyclerView.ViewHolder{
        TextView nameHolder;
        //TextView amountHolder;
        TypedArray imgs;
        ImageView icon;
        ImageView calendar;
        Spinner spinner;
        Boolean flag = true;
        ArrayAdapter adapter;
        SharedPreferences sp;

        Intent salaam;


                PersonViewHolder(final View itemView) {
            super(itemView);
            nameHolder = itemView.findViewById(mygrocerylist.listandshop.R.id.name);
            imgs = itemView.getContext().getResources().obtainTypedArray(mygrocerylist.listandshop.R.array.icons);
            icon = itemView.findViewById(mygrocerylist.listandshop.R.id.icon);
            calendar = itemView.findViewById(mygrocerylist.listandshop.R.id.calendar);
            //spinner = itemView.findViewById(R.id.spinner);
            adapter = new ArrayAdapter(itemView.getContext(), mygrocerylist.listandshop.R.layout.spinner, pop);

            salaam = new Intent(itemView.getContext(), CalendarClass.class);

            calendar.setImageResource(mygrocerylist.listandshop.R.drawable.calendarblack);

            sp = itemView.getContext().getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);

            calendar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if ((calendar.getDrawable().getConstantState() == itemView.getResources().getDrawable(mygrocerylist.listandshop.R.drawable.calendarblack).getConstantState()))
                    {
                        salaam.putExtra("Item", myList.get(getAdapterPosition()));
                        salaam.putExtra("Icon", amount.get(getAdapterPosition()));
                        salaam.putExtra("Pos", getAdapterPosition());
                        flag = false;
                        view.getContext().startActivity(salaam);
                        //calendar.setImageResource(R.drawable.calendargrey);

                    }
                    else{
                        Toast toast = Toast.makeText(view.getContext(), "Notification Canceled.", Toast.LENGTH_LONG);
                        toast.show();

                        cancelAlarm(getAdapterPosition(), context);


                        theCalendar[getAdapterPosition()] = false;
                        MainActivity.storeArray(theCalendar, context);
                        notifyDataSetChanged();
                        ///the state
                        calendar.setImageResource(mygrocerylist.listandshop.R.drawable.calendarblack);
                    }
                }
            });
        }

    }

    public void cancelAlarm(Integer pos, Context context)
    {
        AlarmManager alarms = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, BroadcastManager.class);
        intent.setAction("ALARM");
        int iUniqueId = (int) (System.currentTimeMillis() & 0xfffffff);
        //intent.setData(Uri.parse("http://"+ String.valueOf(iUniqueId)));
        intent.setData(Uri.parse("content://" + myList.get(pos)));
        intent.putExtra("name", myList.get(pos));
        intent.putExtra("icon", amount.get(pos));
        PendingIntent operation = PendingIntent.getBroadcast(context ,0, intent, PendingIntent.FLAG_ONE_SHOT);
        operation.cancel();
        alarms.cancel(operation);
    }
    }

