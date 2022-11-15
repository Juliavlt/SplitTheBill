package ifsp.pdm.julia.splitthebill.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import ifsp.pdm.julia.splitthebill.databinding.ActivityIntegranteBinding
import ifsp.pdm.julia.splitthebill.model.Constant.EXTRA_MEMBER
import ifsp.pdm.julia.splitthebill.model.Integrante
import ifsp.pdm.julia.splitthebill.model.Constant.VIEW_MEMBER
import kotlin.random.Random

class IntegranteActivity : AppCompatActivity() {
    private val acb: ActivityIntegranteBinding by lazy {
        ActivityIntegranteBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(acb.root)

        val receivedContact =  intent.getParcelableExtra<Integrante>(EXTRA_MEMBER)
        receivedContact?.let{ _receivedContact ->
            acb.nomeEt.setText(_receivedContact.nome)
            acb.valorPagoEt.setText(_receivedContact.valorPago.toString())
            acb.itemCompradoEt.setText(_receivedContact.itemComprado?:"")
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
            try {
                val integrante = Integrante(
                    id = receivedContact?.id?: Random(System.currentTimeMillis()).nextInt(),
                    nome = acb.nomeEt.text.toString(),
                    itemComprado = acb.itemCompradoEt.text.toString()?: "",
                    valorPago = acb.valorPagoEt.text.toString().toDouble(),
                    valorAReceber = 0.0,
                    valorAPagar = 0.0
                )

                val resultIntent = Intent()
                resultIntent.putExtra(EXTRA_MEMBER, integrante)
                setResult(RESULT_OK, resultIntent)

            } catch (ex: Exception){
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
            }

            finish()
        }
    }
}