package com.example.adarshbhatt.sqlliteapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {
    static DatabaseHelper myDb;
    EditText username, password;
    Button logBtn, signBtn, voteB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDb = new DatabaseHelper(this);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        logBtn = (Button) findViewById(R.id.login);
        signBtn = (Button) findViewById(R.id.sig);
        voteB = (Button) findViewById(R.id.voteBtn);
        logIn();
        vote();
        signUp();
    }

    public void signUp() {
        signBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, UserSign.class);
                startActivity(i);
            }
        });
    }

    public void logIn() {
        final Cursor USER = myDb.getUser();
        logBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                while(USER.moveToNext()) {
                    if ((USER.getString(1).equals(username.getText().toString()) && USER.getString(2).equals(password.getText().toString()))) {
                        Intent i = new Intent(MainActivity.this, UserActivity.class);
                        i.putExtra("highscore", USER.getString(3));
                        i.putExtra("user", USER.getString(1));
                        startActivity(i);
                        return;
                        //ShowMessage("Marks for " + editName.getText().toString(), marks.getString(4));
                    }
                    else continue;
                }
                Toast.makeText(MainActivity.this, "Username/Password is incorrect", Toast.LENGTH_LONG).show();
            }
        });


    }

/*
    public void viewMarks() {
        viewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Cursor marks = myDb.getUser();

                if (!myDb.existsUser(editName.getText().toString())) {
                    ShowMessage("Error", editName.getText().toString() + " Not Found");
                    return;
                }

                while(marks.moveToNext()) {
                    if (marks.getString(1).equals(editName.getText().toString())) {
                        Intent i = new Intent(MainActivity.this, UserActivity.class);
                        i.putExtra("mark", marks.getString(4));
                        i.putExtra("surname", marks.getString(3));
                        startActivity(i);
                        //ShowMessage("Marks for " + editName.getText().toString(), marks.getString(4));
                    }
                    else continue;
                }
            }
        });

    }


    public void ShowMessage (String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.create();
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class UserSign extends Activity {

        Button signUp;
        EditText user, pass1, pass2;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.signup);
            signUp = (Button) findViewById(R.id.btnSign);
            user = (EditText) findViewById(R.id.newUser);
            pass1 = (EditText) findViewById(R.id.pass1);
            pass2 = (EditText) findViewById(R.id.pass2);
            AddData();
        }

        public void AddData() {
            signUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (((pass1.getText().toString()).equals(pass2.getText().toString())) && !myDb.existsUser(user.getText().toString())) {
                        int isInserted = myDb.insertData(user.getText().toString(), pass1.getText().toString());
                        if (isInserted == -1)
                            Toast.makeText(UserSign.this, "Error adding user", Toast.LENGTH_LONG).show();
                        else if (isInserted == 0) {
                            Toast.makeText(UserSign.this, "User is Added", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(UserSign.this, MainActivity.class);
                            startActivity(i);
                        }
                    } else if (!(pass1.getText().toString()).equals(pass2.getText().toString()))
                        Toast.makeText(UserSign.this, "Passwords dont match, try again", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(UserSign.this, "Username " + user.getText().toString() + " already exists", Toast.LENGTH_LONG).show();
                }
            });

        }
    }

    public static class UserActivity extends Activity {
        TextView userScore;
        Button incrBtn, back;
        String value, user;
        Intent intent;
        int highscore;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.user_page);
            incrBtn = (Button) findViewById(R.id.incrButton);
            back = (Button) findViewById(R.id.back);
            intent = getIntent();
            highscore = 0;
            userScore = (TextView) findViewById(R.id.newScore);
            value = intent.getStringExtra("highscore");
            user = intent.getStringExtra("user");
            userScore.setText("High Score: " + value, TextView.BufferType.NORMAL);
            //Toast.makeText(this, value, Toast.LENGTH_LONG).show();
            onClose();
            incrementScore();

        }

        public void incrementScore() {
            incrBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    highscore++;
                    userScore.setText("High Score: " + highscore);
                }
            });


        }

        public void onClose() {
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myDb.updateScore(user, highscore);
                    Intent i = new Intent(UserActivity.this, MainActivity.class);
                    startActivity(i);
                }
            });
        }
    }

    public static class VotePage extends Activity {
        ImageButton noUp, noDown;
        boolean up, down;
        int voteCount;
        TextView voteTotal;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.votepage);
            noUp = (ImageButton) findViewById(R.id.noupvote);
            noDown = (ImageButton) findViewById(R.id.nodownvote);
            voteCount = getVotes();
            voteTotal = (TextView) findViewById(R.id.votesNum);
            voteTotal.setText(voteCount+"");
            voteSystem();
            //myDb.insert(0);
        }

        public void voteSystem() {
            noUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    voteCount++;
                    voteTotal.setText(voteCount + "");
                    noUp.setImageResource(R.drawable.upvote);
                }
            });

            noDown.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myDb.updateVote(voteCount);
                    Intent i = new Intent(VotePage.this, MainActivity.class);
                    startActivity(i);
                }
            });
        }

        public int getVotes() {
            final Cursor VOTE = myDb.getVote();
            while (VOTE.moveToNext()) {
                return VOTE.getInt(1);
            }
            return 5;
        }

    }


    public void vote() {
        voteB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,VotePage.class);
                startActivity(i);
            }
        });
    }
}
