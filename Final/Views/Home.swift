import SwiftUI
import Charts

struct TaskData: Identifiable {
    let id = UUID()
    let status: String
    let count: Int
}

struct Home: View {
    let completedTasks = 3
    let totalTasks = 5
    
    var body: some View {
        let data = [
            TaskData(status: "Tamamlanan", count: completedTasks),
            TaskData(status: "Kalan", count: totalTasks - completedTasks)
        ]
        
        ScrollView {
            VStack(spacing: 25) {
                VStack(alignment: .leading) {
                    Text("Hoş Geldiniz!")
                        .font(.title)
                        .fontWeight(.bold)
                        .foregroundColor(.white)
                        .padding(.top)
                }
                .frame(maxWidth: .infinity, alignment: .leading)
                .padding(.horizontal)

                VStack(alignment: .leading, spacing: 15) {
                    Text("Günlük İlerleme")
                        .font(.headline)
                        .fontWeight(.semibold)
                        .foregroundColor(.white)
                    
                    VStack(spacing: 8) {
                        ProgressView(value: 0.5, total: 1.0)
                            .progressViewStyle(LinearProgressViewStyle(tint: .green))
                            .frame(height: 8)
                        
                        HStack {
                            Text("\(completedTasks)/\(totalTasks) Görev")
                                .foregroundColor(.gray)
                            Spacer()
                            Text("50%")
                                .fontWeight(.semibold)
                                .foregroundColor(.green)
                        }
                    }
                }
                .padding()
                .background(
                    RoundedRectangle(cornerRadius: 15)
                        .fill(Color.gray.opacity(0.15))
                )
                .padding(.horizontal)

                VStack(alignment: .leading, spacing: 15) {
                    Text("Bugünkü Görevler")
                        .font(.headline)
                        .fontWeight(.semibold)
                        .foregroundColor(.white)
                    
                    TasksList()
                        .frame(height: 200)
                }
                .padding()
                .background(
                    RoundedRectangle(cornerRadius: 15)
                        .fill(Color.gray.opacity(0.15))
                )
                .padding(.horizontal)

                VStack(alignment: .leading, spacing: 15) {
                    Text("İstatistikler")
                        .font(.headline)
                        .fontWeight(.semibold)
                        .foregroundColor(.white)
                    
                    Chart(data) { task in
                        BarMark(
                            x: .value("Durum", task.status),
                            y: .value("Görev Sayısı", task.count)
                        )
                        .foregroundStyle(task.status == "Tamamlanan" ? .green : .red.opacity(0.8))
                    }
                    .frame(height: 200)
                    .chartXAxis {
                        AxisMarks { _ in
                            AxisValueLabel()
                                .foregroundStyle(.white)
                        }
                    }
                    .chartYAxis {
                        AxisMarks { _ in
                            AxisValueLabel()
                                .foregroundStyle(.white)
                        }
                    }
                }
                .padding()
                .background(
                    RoundedRectangle(cornerRadius: 15)
                        .fill(Color.gray.opacity(0.15))
                )
                .padding(.horizontal)

                VStack(alignment: .leading, spacing: 15) {
                    Text("Rozetler")
                        .font(.headline)
                        .fontWeight(.semibold)
                        .foregroundColor(.white)
                    
                    ScrollView(.horizontal, showsIndicators: false) {
                        HStack(spacing: 20) {
                            ForEach(0..<3) { index in
                                VStack(spacing: 10) {
                                    Image(systemName: "trophy.fill")
                                        .resizable()
                                        .scaledToFit()
                                        .frame(width: 40, height: 40)
                                        .foregroundColor(.yellow)
                                        .shadow(color: .yellow.opacity(0.5), radius: 5)
                                    
                                    Text("Rozet \(index + 1)")
                                        .font(.caption)
                                        .fontWeight(.medium)
                                        .foregroundColor(.white)
                                }
                                .padding()
                                .background(
                                    RoundedRectangle(cornerRadius: 15)
                                        .fill(Color.yellow.opacity(0.15))
                                )
                            }
                        }
                    }
                }
                .padding()
                .background(
                    RoundedRectangle(cornerRadius: 15)
                        .fill(Color.gray.opacity(0.15))
                )
                .padding(.horizontal)
            }
            .padding(.vertical)
        }
        .background(Color.black)
    }
}

#Preview {
    Home()
}
