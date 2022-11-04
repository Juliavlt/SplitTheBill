package ifsp.pdm.julia.splitthebill.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import ifsp.pdm.julia.splitthebill.databinding.ActivityIntegranteBinding
import ifsp.pdm.julia.splitthebill.model.Constant.EXTRA_CONTACT
import ifsp.pdm.julia.splitthebill.model.Integrante
import ifsp.pdm.julia.splitthebill.model.Constant.VIEW_CONTACT
import kotlin.random.Random

class IntegranteActivity : AppCompatActivity() {
    private val acb: ActivityIntegranteBinding by lazy {
        ActivityIntegranteBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(acb.root) //seta contentView

        val receivedContact =  intent.getParcelableExtra<Integrante>(EXTRA_CONTACT) //é um Contact | intente = getIntent
        receivedContact?.let{ _receivedContact -> // é a mesma coisa que: if != null | _ indica que esse nao é nulo
            acb.nomeEt.setText(_receivedContact.nome) //quando é edit text tem que ter o set
            acb.valorPagoEt.setText(_receivedContact.valorPago.toString())
            acb.itemCompradoEt.setText(_receivedContact.itemComprado)
        }

        val viewContact = intent.getBooleanExtra(VIEW_CONTACT, false)
        if (viewContact){
            acb.nomeEt.isEnabled = false
            acb.valorPagoEt.isEnabled = false
            acb.itemCompradoEt.isEnabled = false
            acb.salvarBt.visibility = View.GONE
        }

        acb.salvarBt.setOnClickListener{
            val integrante = Integrante(
                id = receivedContact?.id?: Random(System.currentTimeMillis()).nextInt(), //se ja existe o id monda o id senao cria um novo
                nome = acb.nomeEt.text.toString(),
                itemComprado = acb.itemCompradoEt.text.toString(),
                valorPago = acb.valorPagoEt.text.toString().toDouble(),
                valorAReceber = 0.0
            )

            val resultIntent = Intent()
            resultIntent.putExtra(EXTRA_CONTACT, integrante)
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }
}