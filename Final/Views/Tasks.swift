import SwiftUI

struct Tasks: View {
    var body: some View {
        VStack(spacing: 16) {
            HStack {
                Text("Görevler")
                    .font(.title)
                    .fontWeight(.bold)
                    .foregroundColor(.white)
                
                Spacer()
                
                HStack(spacing: 12) {
                    Button(action: {
                    }) {
                        Image(systemName: "magnifyingglass")
                            .foregroundColor(.white)
                    }
                    
                    Button(action: {
                    }) {
                        Image(systemName: "line.3.horizontal.decrease.circle")
                            .foregroundColor(.white)
                    }
                }
            }
            .padding()
            .background(Color.black)
            
            ScrollView(.horizontal, showsIndicators: false) {
                HStack(spacing: 12) {
                    ForEach(["Tümü", "Aktif", "Tamamlanan"], id: \.self) { category in
                        Text(category)
                            .font(.subheadline)
                            .fontWeight(.medium)
                            .padding(.horizontal, 16)
                            .padding(.vertical, 8)
                            .background(
                                RoundedRectangle(cornerRadius: 20)
                                    .fill(category == "Tümü" ? Color.green : Color.gray.opacity(0.15))
                            )
                            .foregroundColor(category == "Tümü" ? .white : .gray)
                    }
                }
                .padding(.horizontal)
            }
            
            // TasksList
            TasksList()
                .background(Color.black)
            
            Spacer()
        }
        .background(Color.black)
        .navigationBarTitleDisplayMode(.inline)
    }
}

#Preview {
    Tasks()
        
}
