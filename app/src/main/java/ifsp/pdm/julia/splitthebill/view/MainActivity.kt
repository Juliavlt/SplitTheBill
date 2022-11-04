package ifsp.pdm.julia.splitthebill.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import ifsp.pdm.julia.splitthebill.R
import ifsp.pdm.julia.splitthebill.adapter.IntegranteAdapter
import ifsp.pdm.julia.splitthebill.databinding.ActivityMainBinding
import ifsp.pdm.julia.splitthebill.model.Constant
import ifsp.pdm.julia.splitthebill.model.Constant.EXTRA_CONTACT
import ifsp.pdm.julia.splitthebill.model.Integrante

class MainActivity : AppCompatActivity() {

    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    // Data source
    private val integrantesList: MutableList<Integrante> = mutableListOf()

    // Adapter
    private lateinit var integranteAdapter: IntegranteAdapter

    private lateinit var carl: ActivityResultLauncher<Intent>//criação do objeto que trata os retorno de telas secundarias

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)

        integranteAdapter= IntegranteAdapter(this, integrantesList) //settando o Adapter com o listView
        amb.integrantesLv.adapter = integranteAdapter

        carl = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
        ) { resultado ->
            if (resultado.resultCode == RESULT_OK) {
                val contact = resultado.data?.getParcelableExtra<Integrante>(EXTRA_CONTACT)
                contact?.let { _contact ->
                    if(integrantesList.any { it.id == _contact.id }){ //se o id ja existe
                        val position = integrantesList.indexOfFirst { it.id == _contact.id } //pegar posição da lista
                        integrantesList[position] = _contact //edita
                    } else {
                        integrantesList.add(_contact)//cria um novo
                    }
                    integranteAdapter.notifyDataSetChanged() //adapter verifica uma modificação da lista
                }
            }
        }
        registerForContextMenu(amb.integrantesLv)
        amb.integrantesLv.onItemClickListener = object: AdapterView.OnItemClickListener{
            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val contact = integrantesList[position]
                val contactIntent = Intent(this@MainActivity, IntegranteActivity::class.java)
                contactIntent.putExtra(EXTRA_CONTACT, contact)
                contactIntent.putExtra(Constant.VIEW_CONTACT, true)
                startActivity(contactIntent)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.addIntegranteMi -> {
                // abre a tela de contato
                carl.launch(Intent(this, IntegranteActivity::class.java))
                true
            }
            else -> { false }
        }
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        menuInflater.inflate(R.menu.context_menu_main, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val position = (item.menuInfo as AdapterView.AdapterContextMenuInfo).position
        return when(item.itemId) {
            R.id.removeIntegranteMi -> {
                integrantesList.removeAt(position)
                integranteAdapter.notifyDataSetChanged()
                //Toast.makeText(this, "Contato removido com sucesso!", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.editIntegranteMi -> {
                val contact = integrantesList[position]
                val contactIntent = Intent(this, IntegranteActivity::class.java) //criar intent pra ir pra proxima tela
                contactIntent.putExtra(EXTRA_CONTACT, contact)
                //startActivity(contactIntent) //so abre a tela
                carl.launch(contactIntent)

                //Toast.makeText(this, "Contato editado com sucesso!", Toast.LENGTH_SHORT).show()
                true
            }

            else -> { false }
        }
    }

}