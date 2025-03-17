import SwiftUI

struct Badge: Identifiable {
    let id = UUID()
    let icon: String
    let name: String
    let description: String
    let isUnlocked: Bool
}

struct Badges: View {
    let badges = [
        Badge(icon: "star.circle.fill", name: "Başlangıç", description: "İlk görevi tamamla", isUnlocked: true),
        Badge(icon: "flame.fill", name: "Ateşli", description: "3 gün üst üste görev tamamla", isUnlocked: true),
        Badge(icon: "bolt.fill", name: "Hızlı", description: "Bir görevi 1 saat içinde tamamla", isUnlocked: true),
        Badge(icon: "trophy.fill", name: "Şampiyon", description: "50 görev tamamla", isUnlocked: false),
        Badge(icon: "crown.fill", name: "Kral", description: "100 görev tamamla", isUnlocked: false),
        Badge(icon: "medal.fill", name: "Uzman", description: "Her kategoriden görev tamamla", isUnlocked: false),
        Badge(icon: "sparkles", name: "Mükemmel", description: "10 görevi zamanında tamamla", isUnlocked: false),
        Badge(icon: "shield.fill", name: "Koruyucu", description: "30 gün aktif ol", isUnlocked: false)
    ]
    
    let columns = [
        GridItem(.adaptive(minimum: 160), spacing: 15)
    ]
    
    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 20) {
                Text("Rozetlerim")
                    .font(.title)
                    .fontWeight(.bold)
                    .foregroundColor(.white)
                    .padding(.horizontal)
                
                HStack {
                    VStack(alignment: .leading) {
                        Text("\(badges.filter { $0.isUnlocked }.count)")
                            .font(.title)
                            .fontWeight(.bold)
                            .foregroundColor(.green)
                        Text("Kazanılan")
                            .foregroundColor(.gray)
                    }
                    Spacer()
                    VStack(alignment: .trailing) {
                        Text("\(badges.count)")
                            .font(.title)
                            .fontWeight(.bold)
                            .foregroundColor(.white)
                        Text("Toplam")
                            .foregroundColor(.gray)
                    }
                }
                .padding()
                .background(Color.gray.opacity(0.15))
                .cornerRadius(15)
                .padding(.horizontal)
                
                LazyVGrid(columns: columns, spacing: 15) {
                    ForEach(badges) { badge in
                        VStack(spacing: 12) {
                            Image(systemName: badge.icon)
                                .font(.system(size: 30))
                                .foregroundColor(badge.isUnlocked ? .yellow : .gray)
                                .frame(width: 60, height: 60)
                                .background(
                                    Circle()
                                        .fill(badge.isUnlocked ? Color.yellow.opacity(0.2) : Color.gray.opacity(0.2))
                                )
                            
                            Text(badge.name)
                                .font(.headline)
                                .foregroundColor(badge.isUnlocked ? .white : .gray)
                            
                            Text(badge.description)
                                .font(.caption)
                                .foregroundColor(.gray)
                                .multilineTextAlignment(.center)
                        }
                        .frame(maxWidth: .infinity)
                        .padding()
                        .background(Color.gray.opacity(0.15))
                        .cornerRadius(15)
                    }
                }
                .padding(.horizontal)
            }
            .padding(.vertical)
        }
        .background(Color.black)
    }
}

#Preview {
    Badges()
}
