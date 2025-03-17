import SwiftUI

struct Task: Identifiable {
    let id = UUID()
    let title: String
    let subtitle: String
    var isCompleted: Bool = false
}

struct TasksList: View {
    @State private var tasks = [
        Task(title: "1. Görev", subtitle: "Görev Adı"),
        Task(title: "2. Görev", subtitle: "Görev Adı"),
        Task(title: "3. Görev", subtitle: "Görev Adı"),
        Task(title: "4. Görev", subtitle: "Görev Adı"),
        Task(title: "5. Görev", subtitle: "Görev Adı"),
        Task(title: "6. Görev", subtitle: "Görev Adı")
    ]
    
    var body: some View {
        List {
            ForEach(tasks) { task in
                HStack {
                    Image(systemName: task.isCompleted ? "checkmark.circle.fill" : "circle")
                        .foregroundColor(task.isCompleted ? .green : .gray)
                        .font(.system(size: 20))
                        .onTapGesture {
                            if let index = tasks.firstIndex(where: { $0.id == task.id }) {
                                tasks[index].isCompleted.toggle()
                            }
                        }
                    
                    VStack(alignment: .leading, spacing: 4) {
                        Text(task.title)
                            .fontWeight(.medium)
                            .foregroundColor(.white)
                        
                        Text(task.subtitle)
                            .font(.caption)
                            .foregroundColor(.gray)
                    }
                    .padding(.leading, 8)
                    
                    Spacer()
                    
                    Image(systemName: "chevron.right")
                        .foregroundColor(.gray)
                        .font(.system(size: 14))
                }
                .padding(.vertical, 8)
            }
            .listRowBackground(Color.gray.opacity(0.15))
            .listRowSeparator(.hidden)
        }
        .listStyle(PlainListStyle())
        .background(Color.black)
        .scrollContentBackground(.hidden)
    }
}

#Preview {
    TasksList()
       
}
