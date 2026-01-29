package in.eoninfotech.eontechnician;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class ChangePassword extends AppCompatActivity  {

    EditText old_password,new_password,confirm_password;
    String old_pass , new_pass , confirm_pass;
    Button change_pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        old_password = findViewById(R.id.old_password);
        new_password = findViewById(R.id.new_password);
        confirm_password = findViewById(R.id.confirm_password);

        change_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               old_pass = old_password.getText().toString();
               new_pass = new_password.getText().toString();
               confirm_pass = confirm_password.getText().toString();

               if(old_pass.equalsIgnoreCase("")){
                   old_password.setError("Fill Old Password");
               }else if(new_pass.equalsIgnoreCase("")){
                   new_password.setError("Fill New Password");
               }else if(confirm_pass.equalsIgnoreCase("")){
                   confirm_password.setError("Fill Confirm Password");
               }else if(old_pass.matches(new_pass)){
                   new_password.setError("Old Password and New Password should not be same");
               } else if(new_pass.matches(confirm_pass)){
                   confirm_password.setError("New Password and Confirm Password does not match");
               }else {
                   changePassword(new_pass,confirm_pass);
               }
            }
        });
    }

    private void changePassword(String new_pass, String confirm_pass) {

    }
}
