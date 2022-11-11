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
import ifsp.pdm.julia.splitthebill.adapter.DetailsAdapter
import ifsp.pdm.julia.splitthebill.databinding.ActivityDetailsBinding
import ifsp.pdm.julia.splitthebill.databinding.ActivityMainBinding
import ifsp.pdm.julia.splitthebill.model.Constant
import ifsp.pdm.julia.splitthebill.model.Constant.EXTRA_MEMBER
import ifsp.pdm.julia.splitthebill.model.Integrante

class MainActivity : AppCompatActivity() {

    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    // Data source
    private val integrantesList: MutableList<Integrante> = mutableListOf()

    // Adapter
    private lateinit var integranteAdapter: DetailsAdapter

    private lateinit var carl: ActivityResultLauncher<Intent>//criação do objeto que trata os retorno de telas secundarias

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)

        integranteAdapter= DetailsAdapter(this, integrantesList) //settando o Adapter com o listView
        amb.integrantesLv.adapter = integranteAdapter

        carl = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
        ) { resultado ->
            if (resultado.resultCode == RESULT_OK) {
                val contact = resultado.data?.getParcelableExtra<Integrante>(EXTRA_MEMBER)
                contact?.let { _contact ->
                    if(integrantesList.any { it.id == _contact.id }){ //se o id ja existe
                        val position = integrantesList.indexOfFirst { it.id == _contact.id } //pegar posição da lista
                        integrantesList[position] = _contact //edita
                    } else {
                        integrantesList.add(_contact)//cria um novo
                    }
                    shareTheBill()
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
                //todo manter integrantes list aqui
                val contact = integrantesList[position]
                val contactIntent = Intent(this@MainActivity, MemberActivity::class.java)
                contactIntent.putExtra(EXTRA_MEMBER, contact)
                contactIntent.putExtra(Constant.VIEW_MEMBER, true)
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
                carl.launch(Intent(this, MemberActivity::class.java))
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
                shareTheBill()
                true
            }
            R.id.editIntegranteMi -> {
                val contact = integrantesList[position]
                val contactIntent = Intent(this, MemberActivity::class.java) //criar intent pra ir pra proxima tela
                contactIntent.putExtra(EXTRA_MEMBER, contact)
                //startActivity(contactIntent) //so abre a tela
                carl.launch(contactIntent)
                true
            } R.id.detailsIntegranteMi -> {
                val contact = integrantesList[position]
                val contactIntent = Intent(this@MainActivity, MemberActivity::class.java)
                contactIntent.putExtra(EXTRA_MEMBER, contact)
                contactIntent.putExtra(Constant.VIEW_MEMBER, true)
                startActivity(contactIntent)
                true
            }

            else -> { false }
        }
    }

    private fun shareTheBill(){
        // Passsar na lista e calcular o total e a quantia dividida
        var valorTotal: Double =  0.0
        for (i in integrantesList) {
            valorTotal+=i.valorPago;
        }
        // Passar na lista novamente e atualizar com os novos valores
        for (i in integrantesList) {
            i.valorAReceber = valorAReceber(integrantesList.size, valorTotal, i.valorPago)
            i.valorAPagar = valorAPagar(integrantesList.size, valorTotal, i.valorPago)
        }
        // Notificar o adapter das alterações
        integranteAdapter.notifyDataSetChanged()
        println(integrantesList)
    }

    fun valorAReceber(numeroIntegrantes: Int, valorTotal: Double, valorPago: Double): Double{
        val valorAReceber: Double;
        if (numeroIntegrantes == 1){
            valorAReceber = 0.0;
        }  else if (valorPago > (valorTotal / numeroIntegrantes)) {
            valorAReceber = ((valorTotal / numeroIntegrantes) - valorPago) * (-1);
        } else{
            valorAReceber = 0.0
        }
        return valorAReceber;
    }

    fun valorAPagar(numeroIntegrantes: Int, valorTotal: Double, valorPago: Double): Double{
        val valorAPagar: Double;
        if (numeroIntegrantes == 1){
            valorAPagar = 0.0;
        } else if (valorPago == 0.0) {
            valorAPagar = valorTotal / numeroIntegrantes;
        }
        else if (valorPago < (valorTotal / numeroIntegrantes)) {
            valorAPagar = (valorTotal / numeroIntegrantes) - valorPago;
        } else {
            valorAPagar = 0.0
        }
        return valorAPagar;
    }
}