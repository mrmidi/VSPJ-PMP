//
//  ContentView.swift
//  CompareIT2
//
//  Created by Alexander Shabelnikov on 5.03.2024.
//

import SwiftUI
import SwiftData



struct ContentView: View {
    @Environment(\.modelContext) private var modelContext
    @Query private var items: [Item]
    
    @State private var showingAddItemView = false
    
    var body: some View {
        NavigationSplitView {
            List {
                ForEach(items) { item in
                    NavigationLink {

                        ProductComparisonView(item: item)
                    } label: {
                        let name = item.name.isEmpty ? "Item" : item.name
                        Text("\(name) at \(item.timestamp, format: Date.FormatStyle(date: .numeric))")
                    }
                }
                .onDelete(perform: deleteItems)
            }
            .toolbar {
                ToolbarItem(placement: .navigationBarTrailing) {
                    EditButton()
                }
                ToolbarItem {

                    Button(action: {
                        showingAddItemView = true
                    }) {
                        Label("Add Item", systemImage: "plus")
                    }
                    
                }
            }
        } detail: {
            Text("Select an item")
        }
        .sheet(isPresented: $showingAddItemView) {
            // AddItemView(modelContext: modelContext, isPresented: $showingAddItemView)
            ItemDetailsView(modelContext: modelContext, isPresented: $showingAddItemView)
        }
        
    }
    
    private func addItem() {
        withAnimation {
            //    let newItem = Item(timestamp: Date())
            // dummy data
            let newItem = Item(
                timestamp: Date(),
                name: "Product",
                price1: 1.0,
                price2: 2.0,
                quantity1: 900.0,
                quantity2: 1700.0,
                unit1: "g",
                unit2: "g"
            )
            modelContext.insert(newItem)
        }
    }
    
    private func deleteItems(offsets: IndexSet) {
        withAnimation {
            for index in offsets {
                modelContext.delete(items[index])
            }
        }
    }
}


struct ItemDetailsView: View {
    var modelContext: ModelContext

    @Binding var isPresented: Bool
    
    @State private var name: String = ""
    
    @State private var price1: Double?
    @State private var quantity1: Double?
    @State private var selectedUnit1: String = "g"
    
    @State private var price2: Double?
    @State private var quantity2: Double?
    @State private var selectedUnit2: String = "g"
    
    let units = ["g", "kg", "ml", "l"]

    @State private var showingAlert = false
    @State private var alertMessage = ""
    
    var body: some View {
        NavigationView {
            Form {
                TextField("Name of Product", text: $name)
                Section(header: Text("Product 1")) {
                    HStack {
                        Text("Price")
                        TextField("Amount", value: $price1, format: .number)
                            .keyboardType(.decimalPad)
                    }
                    HStack {
                        Text("Quantity")
                        TextField("Amount", value: $quantity1, format: .number)
                            .keyboardType(.decimalPad)
                    }
                    Picker("Unit", selection: $selectedUnit1) {
                        ForEach(units, id: \.self) { unit in
                            Text(unit).tag(unit)
                        }
                    }
                    .pickerStyle(SegmentedPickerStyle())
                }
                
                Section(header: Text("Product 2")) {
                    HStack {
                        Text("Price")
                        TextField("Amount", value: $price2, format: .number)
                            .keyboardType(.decimalPad)
                    }
                    HStack {
                        Text("Quantity")
                        TextField("Amount", value: $quantity2, format: .number)
                            .keyboardType(.decimalPad)
                    }
                    Picker("Unit", selection: $selectedUnit2) {
                        ForEach(units, id: \.self) { unit in
                            Text(unit).tag(unit)
                        }
                    }
                    .pickerStyle(SegmentedPickerStyle())
                }
                
            }
            .navigationTitle("Add New Item")
            .navigationBarItems(
                leading: Button("Cancel") {
                    isPresented = false
                },
                trailing: Button("Save") {
                    saveItem()
                }
            )
            .alert(isPresented: $showingAlert) {
                Alert(
                    title: Text("Error"),
                    message: Text(alertMessage),
                    dismissButton: .default(Text("OK"))
                )
            }
        }
    }
    
    private func saveItem() {
        // Check that the units are compatible
        let volumeUnits: Set<String> = ["ml", "l"]
        let massUnits: Set<String> = ["g", "kg"]

        let unitMatch = (volumeUnits.contains(selectedUnit1) && volumeUnits.contains(selectedUnit2)) 
        || (massUnits.contains(selectedUnit1) && massUnits.contains(selectedUnit2))
        
        
        if unitMatch {

            let newItem = Item(
                timestamp: Date(),
                name: name,
                price1: price1 ?? 0,
                price2: price2 ?? 0,
                quantity1: quantity1 ?? 0,
                quantity2: quantity2 ?? 0,
                unit1: selectedUnit1,
                unit2: selectedUnit2
            )

            // Save the item
            modelContext.insert(newItem)
            
            // Close the sheet
            isPresented = false
        } else {
            // Units are not compatible, show an alert
            alertMessage = "Units are not compatible. Please use units of the same type."
            showingAlert = true
        }
    }
    
}

struct ProductComparisonView: View {
    var item: Item

    var body: some View {
        VStack {
            Text(item.name)
                .font(.title)

            // Calculate the price per unit for both products
            let pricePerUnit1 = calculatePricePerUnit(price: item.price1, quantity: item.quantity1, unit: item.unit1)
            let pricePerUnit2 = calculatePricePerUnit(price: item.price2, quantity: item.quantity2, unit: item.unit2)

            // Determine which product is cheaper
            let product1isCheaper = pricePerUnit1 < pricePerUnit2
            
            // Display the prices with colors based on which product is cheaper
            HStack {
                VStack(alignment: .center, spacing: 10) {
                    Text("Product 1")
                        .foregroundColor(product1isCheaper ? .green : .red)
                    Text("\(pricePerUnit1, specifier: "%.2f") per \(item.unit1)")
                        .foregroundColor(product1isCheaper ? .green : .red)

                    Text("Product 2")
                        .foregroundColor(product1isCheaper ? .red : .green)
                    Text("\(pricePerUnit2, specifier: "%.2f") per \(item.unit2)")
                        .foregroundColor(product1isCheaper ? .red : .green)
                }
            }
            .frame(maxWidth: .infinity, maxHeight: .infinity)
        }
        .padding()
        .navigationTitle("Comparison")
        .frame(maxWidth: .infinity, maxHeight: .infinity)
    }
    
    
    // Helper function to calculate the price per standard unit
    func calculatePricePerUnit(price: Double, quantity: Double, unit: String) -> Double {
        var standardizedQuantity = quantity

        // Convert quantity to standardized units (kilograms or liters)
        if unit == "g" {
            standardizedQuantity = quantity / 1000 // Convert grams to kilograms
        } else if unit == "ml" {
            standardizedQuantity = quantity / 1000 // Convert milliliters to liters
        }

        return price / standardizedQuantity // Calculate price per standardized unit
    }

}


#Preview {
    ContentView()
        .modelContainer(for: Item.self, inMemory: true)
}
