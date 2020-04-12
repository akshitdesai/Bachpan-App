package codestromer.com.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class events extends AppCompatActivity {

    private RecyclerView mevent_list;
    private FirebaseFirestore mstore;
    private FirestoreRecyclerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        mevent_list = findViewById(R.id.devent_list);
        mstore = FirebaseFirestore.getInstance();

        //Query
        Query query = mstore.collection("events");//.orderBy("Date");
        //Recycler-options
        FirestoreRecyclerOptions<EventsModel> options = new FirestoreRecyclerOptions.Builder<EventsModel>()
                .setQuery(query,EventsModel.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<EventsModel, EventsViewHolder>(options) {
            @NonNull
            @Override
            public EventsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_single,parent,false);
                return new EventsViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull EventsViewHolder holder, int position, @NonNull EventsModel model) {
                holder.l_name.setText(model.getEventName());
                holder.l_req.setText("Required Volunteer : "+model.getReqiredVol());
                holder.l_desc.setText(model.getDescription());
                holder.l_date.setText(model.getEventdate());
            }
        };
        mevent_list.setHasFixedSize(true);
        mevent_list.setLayoutManager(new LinearLayoutManager(this));
        mevent_list.setAdapter(adapter);

    }
    //View-Holder Class
    private class EventsViewHolder extends RecyclerView.ViewHolder{

        private TextView l_name;
        private TextView l_date;
        private TextView l_desc;
        private TextView l_req;
        public EventsViewHolder(@NonNull View itemView) {
            super(itemView);
            l_name = itemView.findViewById(R.id.dename);
            l_date = itemView.findViewById(R.id.dedate);
            l_desc = itemView.findViewById(R.id.dedescri);
            l_req = itemView.findViewById(R.id.dereq);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }
}
