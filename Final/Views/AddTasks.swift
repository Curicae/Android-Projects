import SwiftUI

struct AddTasks: View {
    @State private var taskName = ""
    @State private var taskDate = Date()
    @State private var priority = 1
    @State private var showAlert = false
    
    let recommendedTasks = [
        (title: "1. Görev", subtitle: "Günlük Egzersiz"),
        (title: "2. Görev", subtitle: "Proje Planlaması"),
        (title: "3. Görev", subtitle: "Okuma Zamanı")
    ]
    
    init() {
        // DatePicker için UIKit stilini dark mode'a zorla
        UIDatePicker.appearance().overrideUserInterfaceStyle = .dark
        UIDatePicker.appearance().backgroundColor = .black
        
        // DatePicker metin rengini beyaz yap
        UILabel.appearance().textColor = .white
    }
    
    var body: some View {
        ScrollView {
            VStack(spacing: 20) {
                // Header
                Text("Yeni Görev")
                    .font(.title)
                    .fontWeight(.bold)
                    .foregroundColor(.white)
                    .padding(.top)
                
                // Form alanları
                VStack(alignment: .leading, spacing: 15) {
                    // Görev Adı
                    VStack(alignment: .leading, spacing: 8) {
                        Text("Görev Adı")
                            .foregroundColor(.white)
                        TextField("Görev adı giriniz", text: $taskName)
                            .textFieldStyle(CustomTextFieldStyle())
                    }
                    
                    // Tarih
                    VStack(alignment: .leading, spacing: 8) {
                        Text("Tarihi")
                            .foregroundColor(.white)
                        ZStack {
                            Color.black // Siyah arka plan
                                .cornerRadius(10)
                            DatePicker("", selection: $taskDate, displayedComponents: .date)
                                .datePickerStyle(CompactDatePickerStyle())
                                .labelsHidden()
                                .padding()
                                .accentColor(.green)
                        }
                        .frame(height: 50)
                    }
                    
                    // Öncelik
                    VStack(alignment: .leading, spacing: 8) {
                        Text("Öncelik")
                            .foregroundColor(.white)
                        Picker("", selection: $priority) {
                            Text("Yüksek")
                                .foregroundColor(.white)
                                .tag(1)
                            Text("Orta")
                                .foregroundColor(.white)
                                .tag(2)
                            Text("Düşük")
                                .foregroundColor(.white)
                                .tag(3)
                        }
                        .pickerStyle(SegmentedPickerStyle())
                        .background(Color.black)
                        .cornerRadius(8)
                    }
                }
                .padding()
                .background(Color.black)
                .cornerRadius(15)
                
                // Kaydet butonu
                Button(action: {
                    showAlert = true
                }) {
                    Text("Görevi Kaydet")
                        .fontWeight(.semibold)
                        .foregroundColor(.white)
                        .frame(maxWidth: .infinity)
                        .padding()
                        .background(Color.green)
                        .cornerRadius(10)
                }
                .padding(.vertical)
                .alert(isPresented: $showAlert) {
                    Alert(
                        title: Text("Görev Kaydedildi"),
                        message: Text("Görev başarıyla eklendi."),
                        dismissButton: .default(Text("Tamam"))
                    )
                }
                
                // Tavsiye edilen görevler
                VStack(alignment: .leading, spacing: 15) {
                    Text("Tavsiye Edilen Görevler")
                        .font(.headline)
                        .fontWeight(.bold)
                        .foregroundColor(.white)
                    
                    ForEach(recommendedTasks, id: \.title) { task in
                        HStack {
                            VStack(alignment: .leading) {
                                Text(task.title)
                                    .foregroundColor(.white)
                                Text(task.subtitle)
                                    .font(.subheadline)
                                    .foregroundColor(.gray)
                            }
                            Spacer()
                            Button(action: {
                                taskName = task.subtitle
                            }) {
                                Image(systemName: "plus.circle.fill")
                                    .foregroundColor(.green)
                                    .font(.title2)
                            }
                        }
                        .padding()
                        .background(Color.black)
                        .cornerRadius(10)
                    }
                }
            }
            .padding()
        }
        .background(Color.black)
        .preferredColorScheme(.dark)
    }
}

struct CustomTextFieldStyle: TextFieldStyle {
    func _body(configuration: TextField<Self._Label>) -> some View {
        configuration
            .padding()
            .background(Color.black)
            .cornerRadius(10)
            .foregroundColor(.white)
    }
}

#Preview {
    AddTasks()
}
