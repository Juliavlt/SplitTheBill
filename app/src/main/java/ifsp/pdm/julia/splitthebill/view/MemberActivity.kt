package ifsp.pdm.julia.splitthebill.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import ifsp.pdm.julia.splitthebill.databinding.ActivityIntegranteBinding
import ifsp.pdm.julia.splitthebill.model.Constant.EXTRA_MEMBER
import ifsp.pdm.julia.splitthebill.model.Integrante
import ifsp.pdm.julia.splitthebill.model.Constant.VIEW_MEMBER
import kotlin.random.Random

class MemberActivity : AppCompatActivity() {
    private val acb: ActivityIntegranteBinding by lazy {
        ActivityIntegranteBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(acb.root) //seta contentView

        val receivedContact =  intent.getParcelableExtra<Integrante>(EXTRA_MEMBER) //é um Contact | intente = getIntent
        receivedContact?.let{ _receivedContact -> // é a mesma coisa que: if != null | _ indica que esse nao é nulo
            acb.nomeEt.setText(_receivedContact.nome) //quando é edit text tem que ter o set
            acb.valorPagoEt.setText(_receivedContact.valorPago.toString())
            acb.itemCompradoEt.setText(_receivedContact.itemComprado)
            acb.valorReceberTv.setText(_receivedContact.valorAReceber.toString())
            acb.valorAPagarTv.setText(_receivedContact.valorAPagar.toString())

        }

        val viewContact = intent.getBooleanExtra(VIEW_MEMBER, false)
        if (viewContact){
            acb.nomeEt.isEnabled = false
            acb.valorPagoEt.isEnabled = false
            acb.itemCompradoEt.isEnabled = false
            acb.salvarBt.visibility = View.GONE
            acb.valorReceberTv.isEnabled = false
            acb.valorAPagarTv.isEnabled = false
            acb.valorReceberTv.visibility = View.VISIBLE
            acb.valorAPagarTv.visibility = View.VISIBLE
        }

        acb.salvarBt.setOnClickListener{
            val integrante = Integrante(
                id = receivedContact?.id?: Random(System.currentTimeMillis()).nextInt(), //se ja existe o id monda o id senao cria um novo
                nome = acb.nomeEt.text.toString()?: "",
                itemComprado = acb.itemCompradoEt.text.toString()?: "",
                valorPago = acb.valorPagoEt.text.toString().toDouble()?: 0.0,
                valorAReceber = 0.0,
                valorAPagar = 0.0
            )

            val resultIntent = Intent()
            resultIntent.putExtra(EXTRA_MEMBER, integrante)
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }
}