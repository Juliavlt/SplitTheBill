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
import ifsp.pdm.julia.splitthebill.adapter.IntegrateAdapter
import ifsp.pdm.julia.splitthebill.databinding.ActivityMainBinding
import ifsp.pdm.julia.splitthebill.model.Constant
import ifsp.pdm.julia.splitthebill.model.Constant.EXTRA_MEMBER
import ifsp.pdm.julia.splitthebill.model.Integrante

class MainActivity : AppCompatActivity() {

    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val integrantesList: MutableList<Integrante> = mutableListOf()

    private lateinit var integranteAdapter: IntegrateAdapter

    private lateinit var carl: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)

        integranteAdapter= IntegrateAdapter(this, integrantesList)
        amb.integrantesLv.adapter = integranteAdapter

        carl = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
        ) { resultado ->
            if (resultado.resultCode == RESULT_OK) {
                val contact = resultado.data?.getParcelableExtra<Integrante>(EXTRA_MEMBER)
                contact?.let { _contact ->
                    if(integrantesList.any { it.id == _contact.id }){
                        val position = integrantesList.indexOfFirst { it.id == _contact.id }
                        integrantesList[position] = _contact
                    } else {
                        integrantesList.add(_contact)
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
                val contact = integrantesList[position]
                val contactIntent = Intent(this@MainActivity, IntegranteActivity::class.java)
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
                shareTheBill()
                true
            }
            R.id.editIntegranteMi -> {
                val contact = integrantesList[position]
                val contactIntent = Intent(this, IntegranteActivity::class.java)
                contactIntent.putExtra(EXTRA_MEMBER, contact)
                carl.launch(contactIntent)
                true
            } R.id.detailsIntegranteMi -> {
                val contact = integrantesList[position]
                val contactIntent = Intent(this@MainActivity, IntegranteActivity::class.java)
                contactIntent.putExtra(EXTRA_MEMBER, contact)
                contactIntent.putExtra(Constant.VIEW_MEMBER, true)
                startActivity(contactIntent)
                true
            }

            else -> { false }
        }
    }

    private fun shareTheBill(){
        var valorTotal: Double =  0.0
        for (i in integrantesList) {
            valorTotal+=i.valorPago;
        }
        for (i in integrantesList) {
            i.valorAReceber = valorAReceber(integrantesList.size, valorTotal, i.valorPago)
            i.valorAPagar = valorAPagar(integrantesList.size, valorTotal, i.valorPago)
        }
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