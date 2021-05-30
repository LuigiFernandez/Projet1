package com.ec.projet1

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private val CAT = "EDPMR"
    private var edtPseudo: EditText? = null
    private var cbRemember: CheckBox? = null
    private var btnOK: Button? = null
    private var sp: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.i(CAT, "onCreate") // trace d'exécution
        btnOK = findViewById(R.id.btnOK)
        edtPseudo = findViewById(R.id.pseudo)
        cbRemember = findViewById(R.id.cbRemember)
        sp = PreferenceManager.getDefaultSharedPreferences(this)
        editor = sp.edit()
        // Demande à MainActivity d'implémenter l'interface onClickListener
        btnOK.setOnClickListener(this)
        cbRemember.setOnClickListener(this)
    }

    // affiche le menu si la méthode renvoie vrai
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        // R.menu.menu dénote le fichier  res/menu/menu.xml
        return true
    }

    // click sur un item du menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.menu_network -> {
                val iReseau = Intent(this, Reseau::class.java)
                startActivity(iReseau)
            }
            R.id.menu_settings -> {
                //TODO : afficher l'activité GestionPreferences
                alerter("Menu : click sur Préférences")
                val iGP = Intent(this, GestionPreferences::class.java)
                startActivity(iGP)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun alerter(s: String) {
        Log.i(CAT, s)
        val t = Toast.makeText(this, s, Toast.LENGTH_SHORT)
        t.show()
    }

    override fun onStart() {
        super.onStart()
        // Relire les préférences partagées de l'application
        val cbR = sp!!.getBoolean("remember", false)
        // actualise l'état de la case à cocher
        cbRemember!!.isChecked = cbR
        // SI la case est cochée, on utilise les préférences pour définir le login
        if (cbRemember!!.isChecked) {
            val l = sp!!.getString("login", "login inconnu")
            edtPseudo!!.setText(l)
        } else {
            // Sinon, le champ doit être vide
            edtPseudo!!.setText("")
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.cbRemember -> {
                alerter("click sur CB")
                // On clique sur la case : il faut mettre à jour les préférences
                editor!!.putBoolean("remember", cbRemember!!.isChecked)
                editor!!.commit()
                if (!cbRemember!!.isChecked) {
                    // on supprime le login des préférences
                    editor!!.putString("login", "")
                    editor!!.commit()
                }
            }
            R.id.btnOK -> {
                alerter("Click sur btnOK")
                // TODO : si la case est cochée,
                // on enregistre le login dans les préférences
                if (cbRemember!!.isChecked) {
                    editor!!.putString("login", edtPseudo!!.text.toString())
                    editor!!.commit()
                }
                // Fabrication d'un Bundle de données
                val bdl = Bundle()
                bdl.putString("json", edtPseudo!!.text.toString())
                // Changer d'activité
                val versSecondAct: Intent
                // Intent explicite
                versSecondAct = Intent(this@MainActivity, SecondeActivity::class.java)
                // Ajout d'un bundle à l'intent
                versSecondAct.putExtras(bdl)
                startActivity(versSecondAct)
            }
            R.id.pseudo -> alerter("Veuillez entrer votre pseudo")
        }
    }
}