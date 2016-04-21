package samsung.defaultmediaplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.samsung.multiscreen.Service;

/**
 * @author Ankit Saini
 * Adapter class to populate tv list (services).
 */
public class TVListAdapter extends ArrayAdapter<Service>
{
    private Context mContext;
    private int mLayoutResourceId;
    private LayoutInflater mInflater;

    public TVListAdapter(Context context, int resourceId)
    {
        super(context, resourceId);
        this.mContext = context;
        this.mLayoutResourceId = resourceId;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public boolean contains(Service service)
    {
        return (getPosition(service) >= 0);
    }

    public void replace(Service service)
    {
        int position = getPosition(service);
        if (position >= 0)
        {
            remove(service);
            insert(service, position);
        }
    }

    class ViewHolder
    {
        TextView name;
        TextView ip;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder;

        if (convertView == null)
        {
            holder = new ViewHolder();

            convertView = mInflater.inflate(mLayoutResourceId, parent, false);
            holder.name = (TextView) convertView.findViewById(R.id.tvName);
            holder.ip = (TextView) convertView.findViewById(R.id.tvIP);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        Service service = getItem(position);
        holder.name.setText(service.getName());
        holder.ip.setText(service.getUri().toString());

        return convertView;
    }
}

