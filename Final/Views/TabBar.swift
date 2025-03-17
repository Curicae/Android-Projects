import SwiftUI

struct TabBar: View {
    @State private var selectedTab = 0
    
    let tabSelectedColor: Color = .green
    let tabUnselectedColor: Color = .gray
    let backgroundColor: Color = .black
    
    var body: some View {
        TabView(selection: $selectedTab) {
            NavigationStack {
                Home()
                    .navigationBarTitleDisplayMode(.inline)
            }
            .tabItem {
                Label("Ana Sayfa", systemImage: selectedTab == 0 ? "house.fill" : "house")
            }
            .tag(0)
            
            NavigationStack {
                Tasks()
                    .navigationBarTitleDisplayMode(.inline)
            }
            .tabItem {
                Label("Görevler", systemImage: selectedTab == 1 ? "checklist" : "checklist")
            }
            .tag(1)
            
            NavigationStack {
                AddTasks()
                    .navigationBarTitleDisplayMode(.inline)
            }
            .tabItem {
                Image(systemName: "plus.circle.fill")
                    .environment(\.symbolRenderingMode, .hierarchical)
                Text("Ekle")
            }
            .tag(2)
            
            
            
            NavigationStack {
                Badges()
                    .navigationBarTitleDisplayMode(.inline)
            }
            .tabItem {
                Label("Rozetler", systemImage: selectedTab == 4 ? "trophy.fill" : "trophy")
            }
            .tag(3)
            
            NavigationStack {
                Settings()
                    .navigationBarTitleDisplayMode(.inline)
            }
            .tabItem {
                Label("Profil", systemImage: selectedTab == 3 ? "person.fill" : "person")
            }
            .tag(4)
        }
        .tint(tabSelectedColor)
        .onAppear {
            let appearance = UITabBarAppearance()
            appearance.configureWithDefaultBackground()
            appearance.backgroundColor = UIColor.black
            
            appearance.stackedLayoutAppearance.normal.iconColor = UIColor(tabUnselectedColor)
            appearance.stackedLayoutAppearance.normal.titleTextAttributes = [
                .foregroundColor: UIColor(tabUnselectedColor)
            ]
            
            appearance.stackedLayoutAppearance.selected.iconColor = UIColor(tabSelectedColor)
            appearance.stackedLayoutAppearance.selected.titleTextAttributes = [
                .foregroundColor: UIColor(tabSelectedColor)
            ]
            
            UITabBar.appearance().standardAppearance = appearance
            UITabBar.appearance().scrollEdgeAppearance = appearance
        }
        
    }
}

#Preview {
    TabBar()
}
