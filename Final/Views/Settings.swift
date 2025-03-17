import SwiftUI

struct Settings: View {
    @State private var notifications = true
    @State private var soundEnabled = true
    @State private var username = "Curicae"
    @State private var showLogoutAlert = false
    
    var body: some View {
        ScrollView {
            VStack(spacing: 20) {
                VStack(spacing: 15) {
                    Image(systemName: "person.circle.fill")
                        .resizable()
                        .aspectRatio(contentMode: .fit)
                        .frame(width: 80, height: 80)
                        .foregroundColor(.green)
                    
                    Text(username)
                        .font(.title2)
                        .fontWeight(.semibold)
                        .foregroundColor(.white)
                }
                .padding()
                .frame(maxWidth: .infinity)
                .background(
                    RoundedRectangle(cornerRadius: 15)
                        .fill(Color.gray.opacity(0.15))
                )
                .padding(.horizontal)
                
                VStack(spacing: 5) {
                    GroupBox {
                        Toggle("Bildirimler", isOn: $notifications)
                            .foregroundColor(.gray)
                        Toggle("Ses", isOn: $soundEnabled)
                            .foregroundColor(.gray)
                    } label: {
                        Label("Görünüm", systemImage: "paintbrush.fill")
                            .foregroundColor(.green)
                    }
                    .padding(.horizontal)
                    .groupBoxStyle(TransparentGroupBox())
                    
                    GroupBox {
                        NavigationLink {
                            Text("Profil Düzenle")
                        } label: {
                            HStack {
                                Label("Profili Düzenle", systemImage: "person.fill")
                                    .foregroundColor(.gray)
                                Spacer()
                                Image(systemName: "chevron.right")
                                    .font(.caption)
                                    .foregroundColor(.gray)
                            }
                        }
                    } label: {
                        Label("Hesap", systemImage: "person.crop.circle.fill")
                            .foregroundColor(.green)
                    }
                    .padding(.horizontal)
                    .groupBoxStyle(TransparentGroupBox())
                    
                    GroupBox {
                        NavigationLink {
                            Text("Hakkında")
                        } label: {
                            HStack {
                                Label("Hakkında", systemImage: "info.circle.fill")
                                    .foregroundColor(.gray)
                                Spacer()
                                Image(systemName: "chevron.right")
                                    .font(.caption)
                                    .foregroundColor(.gray)
                            }
                        }
                        
                        NavigationLink {
                            Text("Yardım")
                        } label: {
                            HStack {
                                Label("Yardım", systemImage: "questionmark.circle.fill")
                                    .foregroundColor(.gray)
                                Spacer()
                                Image(systemName: "chevron.right")
                                    .font(.caption)
                                    .foregroundColor(.gray)
                            }
                        }
                    } label: {
                        Label("Uygulama", systemImage: "app.fill")
                            .foregroundColor(.green)
                    }
                    .padding(.horizontal)
                    .groupBoxStyle(TransparentGroupBox())
                }
                
                Button(action: {
                    showLogoutAlert = true
                }) {
                    Text("Çıkış Yap")
                        .fontWeight(.semibold)
                        .foregroundColor(.red)
                        .frame(maxWidth: .infinity)
                        .padding()
                        .background(
                            RoundedRectangle(cornerRadius: 12)
                                .fill(Color.red.opacity(0.1))
                        )
                }
                .padding(.horizontal)
                .alert("Çıkış Yap", isPresented: $showLogoutAlert) {
                    Button("İptal", role: .cancel) { }
                    Button("Çıkış Yap", role: .destructive) {
                    }
                } message: {
                    Text("Hesabınızdan çıkış yapmak istediğinize emin misiniz?")
                }
            }
            .padding(.vertical)
        }
        .background(Color.black)
        .navigationBarTitleDisplayMode(.inline)
    }
}

struct TransparentGroupBox: GroupBoxStyle {
    func makeBody(configuration: Configuration) -> some View {
        VStack {
            configuration.label
            configuration.content
        }
        .padding()
        .background(Color.gray.opacity(0.15))
        .cornerRadius(8)
    }
}

#Preview {
    Settings()
}
