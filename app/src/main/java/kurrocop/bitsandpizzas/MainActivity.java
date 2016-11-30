package kurrocop.bitsandpizzas;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import values.PizzaFragment;


public class MainActivity extends Activity {
    private String[] title;
    private ListView drawerList;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ActionBar actionBar;

    private  class listener implements  ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // do something
            selectItem(position);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        title = getResources().getStringArray(R.array.title);
        drawerList = (ListView)findViewById(R.id.drawer);
        drawerLayout = (DrawerLayout)findViewById(R.id.activity_main);
        drawerList.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_activated_1,
                title));
        drawerList.setOnItemClickListener(new listener());
        if(savedInstanceState == null){
            selectItem(0);
        }
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.string.open_drawer, R.string.close_drawer){
            @Override
            public void onDrawerClosed(View view){
                super.onDrawerClosed(view);
                // re-create menu items ( makes them invisible)
                invalidateOptionsMenu();
            }
            @Override
            public void onDrawerOpened(View view){
                super.onDrawerOpened(view);
                //re-create menu items ( makes them visible)
                invalidateOptionsMenu();
            }
        };
        drawerLayout.addDrawerListener(drawerToggle);

    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        //inflate the menu
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.findItem(R.id.share_action_provider);
        ShareActionProvider shareActionProvider= (ShareActionProvider)menuItem.getActionProvider();
        // thiet lap intent cua ShareActionProvider
        Intent intentShare= new Intent();
        intentShare.setAction(Intent.ACTION_SEND);
        intentShare.setType("text/plain");
        intentShare.putExtra(Intent.EXTRA_TEXT, "Hello share share share share");
        shareActionProvider.setShareIntent(intentShare);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        boolean drawerOpen= drawerLayout.isDrawerOpen(drawerList);
        menu.findItem(R.id.action_create_order).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(drawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        Intent intent;
        switch (item.getItemId()){
          case R.id.action_create_order:
              intent = new Intent(this, OrderActivity.class);
              startActivity(intent);
              return true;
          case R.id.action_search_web:
               intent = new Intent().setAction(Intent.ACTION_WEB_SEARCH);
              startActivity(intent);
              return true;
          default: return super.onOptionsItemSelected(item);
      }
    }
    private void selectItem(int position){
        Fragment fragment;
        switch (position){
            case 1 : fragment = new PizzaFragment();
                break;
            case 2 : fragment = new PastaFragment();
                break;
            case 3 : fragment = new StoreFragment();
                break;
            default: fragment = new TopFragment();
        }
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        ft.addToBackStack(null);
        ft.setTransition(ft.TRANSIT_FRAGMENT_FADE);
        ft.commit();
        drawerLayout.closeDrawer(drawerList);
        setActionBarTittle(position);
    }
    private void setActionBarTittle(int position){
        String tit;
        if(position == 0)
           tit = getResources().getString(R.string.app_name);
        else {
            tit = title[position];
        }
        if(tit != null) {
            getActionBar().setTitle(tit);
        }
    }
}