package vn.edu.hust.studentman

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

  private lateinit var studentAdapter: StudentAdapter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    val students = mutableListOf(
      StudentModel("Nguyễn Văn An", "SV001"),
      StudentModel("Trần Thị Bảo", "SV002"),
      StudentModel("Lê Hoàng Cường", "SV003"),
      StudentModel("Phạm Thị Dung", "SV004"),
      StudentModel("Đỗ Minh Đức", "SV005"),
      StudentModel("Vũ Thị Hoa", "SV006"),
      StudentModel("Hoàng Văn Hải", "SV007"),
      StudentModel("Bùi Thị Hạnh", "SV008"),
      StudentModel("Đinh Văn Hùng", "SV009"),
      StudentModel("Nguyễn Thị Linh", "SV010"),
      StudentModel("Phạm Văn Long", "SV011"),
      StudentModel("Trần Thị Mai", "SV012"),
      StudentModel("Lê Thị Ngọc", "SV013"),
      StudentModel("Vũ Văn Nam", "SV014"),
      StudentModel("Hoàng Thị Phương", "SV015"),
      StudentModel("Đỗ Văn Quân", "SV016"),
      StudentModel("Nguyễn Thị Thu", "SV017"),
      StudentModel("Trần Văn Tài", "SV018"),
      StudentModel("Phạm Thị Tuyết", "SV019"),
      StudentModel("Lê Văn Vũ", "SV020")
    )

     studentAdapter = StudentAdapter(students, object : StudentAdapter.OnItemClickListener {
      override fun onEditClick(student: StudentModel, position: Int) {
        showEditDialog(student, position, studentAdapter)
      }

      override fun onRemoveClick(student: StudentModel, position: Int) {
          AlertDialog.Builder(this@MainActivity)
            .setTitle("Ban co muon xoa khong")
            .setMessage("Xoa sinh vien : ${student.studentName} co ma sinh vien ${student.studentId}")
            .setPositiveButton("Yes", {_, _ ->
              val removeStudent = students[position]
              students.removeAt(position)
              studentAdapter.notifyItemRemoved(position)
              studentAdapter.notifyItemRangeChanged(position, students.size)

              Snackbar.make(findViewById(R.id.recycler_view_students), "Da xoa thong tin sinh vien", Snackbar.LENGTH_LONG)
                .setAction("UNDO", {
                  students.add(position, removeStudent)
                  studentAdapter.notifyItemInserted(position)
                })
                .show()
            })
            .setNegativeButton("No", null)
            .show()
            .setCanceledOnTouchOutside(false)
//        students.removeAt(position)
//        studentAdapter.notifyItemRemoved(position)
//        studentAdapter.notifyItemRangeChanged(position, students.size)
      }
    })

    findViewById<RecyclerView>(R.id.recycler_view_students).run {
      adapter = studentAdapter
      layoutManager = LinearLayoutManager(this@MainActivity)

    }

    findViewById<Button>(R.id.btn_add_new).setOnClickListener{
      val dialogView = LayoutInflater.from(this)
        .inflate(R.layout.layout_alert_dialog, null)

      val addhoten =dialogView.findViewById<EditText>(R.id.type_hoten)
      val addmssv = dialogView.findViewById<EditText>(R.id.type_mssv)

    AlertDialog.Builder(this)
      .setTitle("Nhap thong tin sinh vien")
      .setView(dialogView)
      .setPositiveButton("Add", {_, _ ->
        val hoten = addhoten.text.toString()
        val mssv =addmssv.text.toString()
        students.add(StudentModel(hoten, mssv))
      })
      .setNegativeButton("cancel", null)
      .show()
    }
  }
  private fun showEditDialog(student: StudentModel, position: Int, adapter: StudentAdapter) {
    val dialogView = LayoutInflater.from(this).inflate(R.layout.layout_edit_dialog, null)
    val editTextName = dialogView.findViewById<EditText>(R.id.edit_hoten)
    val editTextId = dialogView.findViewById<EditText>(R.id.edit_mssv)

    editTextName.setText(student.studentName)
    editTextId.setText(student.studentId)

    AlertDialog.Builder(this)
      .setTitle("Edit Information")
      .setMessage("Chinh sua thong tin cua Sinh vien ${student.studentName} co mssv ${student.studentId}")
      .setView(dialogView)
      .setPositiveButton("Confirm", {_, _ ->
        val newName = editTextName.text.toString()
        val newId = editTextId.text.toString()
        if (newName.isNotBlank() && newId.isNotBlank()) {
          student.studentName = newName
          student.studentId = newId
          adapter.notifyItemChanged(position)
        }
      })
      .setNegativeButton("Cancel", null)
      .show()
  }
}