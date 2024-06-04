//
//  Item.swift
//  CompareIT2
//
//  Created by Alexander Shabelnikov on 5.03.2024.
//

import Foundation
import SwiftData

@Model
final class Item {

    @Attribute(.unique) var id: UUID = UUID() // Unique identifier for each item
    var timestamp: Date
    var name: String // Name of the comparison or item
    var price1: Double // Price of product 1
    var price2: Double // Price of product 2
    var quantity1: Double // Quantity of product 1
    var quantity2: Double // Quantity of product 2
    var unit1: String // Unit for product 1
    var unit2: String // Unit for product 2

    init(timestamp: Date, name: String, price1: Double, price2: Double, quantity1: Double, quantity2: Double, unit1: String, unit2: String) {
        self.timestamp = timestamp
        self.name = name
        self.price1 = price1
        self.price2 = price2
        self.quantity1 = quantity1
        self.quantity2 = quantity2
        self.unit1 = unit1
        self.unit2 = unit2
    }
}
