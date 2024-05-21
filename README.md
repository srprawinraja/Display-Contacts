Manifest
First, add the contact read permission in the manifest file. Permission also needs to be asked for at runtime. We will cover that later in this article.

<uses-permission android:name="android.permission.READ_CONTACTS" />
User Interface
Create a list view inside the layout in the XML file.

activity_main.xml :

<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">
    <ListView
        android:id="@+id/list"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        />
</RelativeLayout>
Permission
In the onCreate() method, add the ListView instance and check if the user has granted contact permission. If not, request the permission.

listView = findViewById(R.id.list); // Initialize the ListView
// Check if READ_CONTACTS permission is not granted
if (checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
    // Request READ_CONTACTS permission
    requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 101);
  } else {
      retriveContacts(new ArrayList<>(),new ArrayList<>());
  }
Declare and implement the onRequestPermissionsResult() override method to handle the user's response to the permission request

 @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Check if the request code matches the READ_CONTACTS permission request
        if (requestCode == 101) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // If permission is granted, retrieve contacts
                retriveContacts(new ArrayList<>(),new ArrayList<>());
            } else {
                // Permission denied, handle accordingly (optional: show a message)
            }
        }
    }

MainActivity.java


public class MainActivity extends AppCompatActivity {
  private ListView listView;
  @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        
        // Check if the request code matches the READ_CONTACTS permission request
        if (requestCode == 101) {
            
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // If permission is granted, retrieve contacts
            } else {
                // Permission denied, handle accordingly (optional: show a message)
            }
        }
    }

  protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);
      listView = findViewById(R.id.list); // Initialize the ListView
      // Check if READ_CONTACTS permission is not granted
      if (checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
           // Request READ_CONTACTS permission
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 101);
      } else {
            // Permission already granted, display contacts
        }
   }
   
}
Adapter
Before retrieving contacts, we need an adapter to connect the UI component and data sources.

Create a new XML file to specify the layout for each item in the list view. In the new XML file, add the two text views inside a linear layout to display the name and phone number in the user interface.

contact_ui.xml

 <LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
        android:layout_height="wrap_content"
        android:layout_width="wrap_content"
        android:orientation="vertical"
        android:translationX="30sp" >

        <TextView
            android:id="@+id/name"
            android:textColor="@color/black"
            android:layout_width="wrap_content"
            android:textSize="20sp"
            android:layout_height="wrap_content"
            android:layout_marginStart="10dp"
            android:padding="2dp"
            android:layout_marginTop="5sp"/>

    <TextView
        android:id="@+id/ph"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:padding="2dp"
        android:layout_marginStart="10dp" />
    </LinearLayout>
Next, to pass the data from the data source to UI components, we need a list adapter that helps to pass data to the list view, so create a new Java class. It must extend with ArrayAdapter<String> and have a constructor with matching super.


public class contact_adapter extends ArrayAdapter<String> {
    // Constructor for contact_adapter
    public contact_adapter(Activity context, List<String> names, List<String> ph) {
        // Call the superclass constructor with context, layout resource, and the list of phone numbers
        super(context, R.layout.contact_ui, ph);
    }
}
Declare global containers and context, and then assign them inside the constructor.

public class contact_adapter extends ArrayAdapter<String> {
    // Global variables to store context, names, and phone numbers
    private Activity context;
    private List<String> name;
    private List<String> ph;

    // Constructor for contact_adapter
    public contact_adapter(Activity context, List<String> names, List<String> ph) {
        // Call the superclass constructor with context, layout resource, and the list of phone numbers
        super(context, R.layout.contact_ui, ph);

        // Assign the passed parameters to the global variables
        this.context = context;
        this.name = names;
        this.ph = ph;
    }
}
Now include the getView() override method below the constructor and utilize the LayoutInflater class to instantiate the contents of layout XML files.

@NonNull
@Override
public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
    // Get the LayoutInflater from the context (Activity) to inflate the custom layout
    LayoutInflater inflater = context.getLayoutInflater();
    
    // Inflate a new view using the contact_ui layout resource.
    // The @SuppressLint annotation is used to suppress specific lint warnings:
    // - "ViewHolder" warning: Suggests using the ViewHolder pattern to improve performance.
    // - "InflateParams" warning: Warns about using `null` as the root parameter in inflate method.
    @SuppressLint({"ViewHolder", "InflateParams"})
    View rowView = inflater.inflate(R.layout.contact_ui, null, true);

    // Return the inflated view (rowView) that represents a single item in the list.
    return rowView;
}
After that, we need to find the contact_ui.xml TextView elements in the rowView and set the text for the text view.

 @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        // Inflate a new view using the contact_ui layout resource.
        @SuppressLint({"ViewHolder", "InflateParams"})
        View rowView = inflater.inflate(R.layout.contact_ui, null, true);

        // Find the TextView elements in the rowView
        TextView Textname = (TextView) rowView.findViewById(R.id.name);
        TextView Textph = (TextView) rowView.findViewById(R.id.ph);

        // Set the text for the TextView elements with the corresponding data
        Textname.setText(name.get(position));
        Textph.setText(ph.get(position));

        // Return the populated view
        return rowView;
    }
contact_adapter.java

public class contact_adapter extends ArrayAdapter<String> {
    private Activity context;
    private List<String> name;
    private List<String> ph;
    public contact_adapter(Activity context,List<String> names, List<String>ph) {
        super(context, R.layout.contact_ui, ph);
        this.context=context;
        this.name=names;
        this.ph=ph;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        @SuppressLint({"ViewHolder", "InflateParams"}) View rowView=inflater.inflate(R.layout.contact_ui, null,true);
        TextView Textname = (TextView) rowView.findViewById(R.id.name);
        TextView Textph =(TextView)  rowView.findViewById(R.id.ph);
        Textname.setText(name.get(position));
        Textph.setText(ph.get(position));
        return rowView;
    }
}
Set Adapter
Create and implement a new method to set the adapter to display the retrieved contacts.

Inside the setAdapter() method, create a new instance of the ContactAdapter class and assign it to a local variable named contactAdapter with the parameter.

Set the newly created adapter (contactAdapter) to the listView.

private void setAdapter(List<String> name,List<String> number) {
    contact_adapter=new contact_adapter(this,name,number);
    listView.setAdapter(contact_adapter);
}
This method will be called from the retrieveContacts() method.

Retrieve Contacts
In MainActivity, we’ll implement a method to retrieve user contacts.

We’ll obtain a ContentResolver instance from the current Context to access content providers, which manage structured data access.

Subsequently, we’ll initiate a query on the specified CONTENT_URI to retrieve contact information.

// Method to retrieve contacts and populate the provided lists with names and numbers
private void retriveContacts(List<String> name, List<String> number) {
    // Get the ContentResolver instance from the current context
    ContentResolver resolver = getContentResolver();

    // Perform a query to retrieve contact information from the device's contacts
    Cursor cursor = resolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI, // URI for accessing contact data
            null, // Projection: retrieve all columns
            null, // Selection: no filtering
            null, // SelectionArgs: no arguments
            ContactsContract.Contacts.DISPLAY_NAME + " ASC" // Sort order: by display name in ascending order
    );
}
To retrieve the contact name and phone number column index, we’ll loop through the cursor to get the contact details from the cursor’s current row using the column index.

// Retrieve the column indices for the display name and phone number
int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

// Iterate over the cursor's result set
while (cursor.moveToNext()) {
    // Retrieve the contact name and phone number from the cursor and add them to the lists
    name.add(cursor.getString(nameIndex));
    number.add(cursor.getString(numberIndex));
}
// Close the cursor to release resources
cursor.close();
setAdapter(name,number); // method called to set adapter
here the cursor used to move from row to row.

MainActivity.java


public class MainActivity extends AppCompatActivity {


    private ListView listView;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Check if the request code matches the READ_CONTACTS permission request
        if (requestCode == 101) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // If permission is granted, retrieve contacts
                retriveContacts(new ArrayList<>(),new ArrayList<>());
            } else {
                // Permission denied, handle accordingly (optional: show a message)
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.list); // Initialize the ListView
        // Check if READ_CONTACTS permission is not granted
        if (checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            // Request READ_CONTACTS permission
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 101);
        } else {
            retriveContacts(new ArrayList<>(),new ArrayList<>());
        }
    }



    // Method to retrieve contacts and populate the provided lists with names and numbers
    private void retriveContacts(List<String> name, List<String> number) {
        // Get the ContentResolver instance from the current context
        ContentResolver resolver = getContentResolver();

        // Perform a query to retrieve contact information from the device's contacts
        Cursor cursor = resolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, // URI for accessing contact data
                null, // Projection: retrieve all columns
                null, // Selection: no filtering
                null, // SelectionArgs: no arguments
                ContactsContract.Contacts.DISPLAY_NAME + " ASC" // Sort order: by display name in ascending order
        );
        // Retrieve the column indices for the display name and phone number
        int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

// Iterate over the cursor's result set
        while (cursor.moveToNext()) {
            // Retrieve the contact name and phone number from the cursor and add them to the lists
            name.add(cursor.getString(nameIndex));
            number.add(cursor.getString(numberIndex));
        }
        cursor.close();
        setAdapter(name,number);
    }
    private void setAdapter(List<String> name, List<String> number) {

        // Creating a new instance of contact_adapter locally within the method
        contact_adapter contact_adapter = new contact_adapter(this, name, number);
        // Setting the newly created adapter to the listView
        listView.setAdapter(contact_adapter);
    }


}
That’s all. Now, check on your emulator or mobile phone. It should display the contact name and phone number.
