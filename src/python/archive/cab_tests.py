#!/usr/bin/python3.5

import sys
import unittest
import inspect
from cabprocessor import Cab_Data
from pathlib import Path
from time import time
from helpers import create_transactions

from vegvisir import Transaction, Block
from distance_aggregation import *

# Author: Gloire Rubambiza
# Version 11/28/2018

# @brief: a class for running cab simulation tests
class TestCabTransactions(unittest.TestCase):

    def test_create_tx(self):
        print("Test started: %s" % inspect.stack()[0][3])
        data_path = Path("cabspottingdata/new_abboip.txt")
        fd = open(str(data_path), encoding='utf-8')
        
        block_data = create_transactions('new_abboip', fd, 10)

        # Test that we read enough data
        self.assertEqual(len(block_data['tx']), 10)
        self.assertFalse(block_data['inc'])

        # Tests location has correct values
        latitude = block_data['loc']['latitude']
        self.assertEqual(latitude, 37.75134)

        # Close the file descriptor
        fd.close()

        # Tests that a transaction is created
        tx = block_data['tx'][0]
        self.assertIsInstance(tx, Transaction, msg="Transaction test pass?")
        print("Test passed: %s" % inspect.stack()[0][3])
    
    def test_create_block(self):
        print("Test started: %s" % inspect.stack()[0][3])

        data_path = Path("cabspottingdata/new_abboip.txt")
        fd = open(str(data_path), encoding='utf-8')
        
        block_data = create_transactions('new_abboip', fd, 10)

        # Test that we read enough data
        self.assertEqual(len(block_data['tx']), 10)
        self.assertFalse(block_data['inc'])

        # Close the file descriptor
        fd.close()

        # Tests that a block is created
        location = block_data['loc']
        user_id = location['id']
        parents = ["beavis", "butthead"]
        block = Block(user_id, time(), parents, block_data['tx'], location)
        self.assertIsInstance(block, Block, msg="Block was created properly!")
        self.assertEqual(block.location['latitude'], 37.75134)
        print("Test passed: %s" % inspect.stack()[0][3])
    # End of test_create_block

    def test_find_maximum(self):
        print("Test started: %s" % inspect.stack()[0][3])
        p = Cab_Data("_cabs.txt")
        p.cab_triage(20000, 200)

        local_maxs = p.find_data_maximum("extremes.txt")

        self.assertIsInstance(local_maxs['max_lat'], float)
        self.assertGreater(local_maxs['max_lat'], local_maxs['min_lat'])
        self.assertLessEqual(local_maxs['min_long'], local_maxs['max_lat'])
        print("Test passed: %s" % inspect.stack()[0][3])

    def test_finding_popular_neighbors(self):
        print("Test started: %s" % inspect.stack()[0][3])

        prelim = Cab_Data('_cabs.txt')

        extremes = prelim.read_data_extremes("extremes.txt")
        print('Extremes %s\n' % extremes)

        neighbor_finder = Neighbors(extremes['max_lat'], extremes['max_long'], 
                                    extremes['min_lat'], extremes['min_long'])

        prelim.cab_triage(20000, 200)
        print("Total users selected -> %d\n" % len(prelim.cab_struct))

        # data_path = prelim.data_path

        user_data = {}
        for cab_id, cab_data in prelim.cab_struct.items():
            # filename = data_path / (prelim.cab_file_names[user])
            fd = cab_data[1]

            block_data = create_transactions(cab_id, fd, 10)
            user_data[cab_id] = block_data['loc']
        
        prelim.close_file_descriptors()
        
        total_users_hvsn = 0
        total_users_norm = 0
        hvsn_dist = 300
        norm_dist = 0.0001
        for _, user_info in user_data.items():
            # Find users who are within x ft of current user
            nearby_users = neighbor_finder.find_by_haversine(user_info,
                        user_data, hvsn_dist)
            # Test that all users fall within the specified limit
            if nearby_users:
                for _, data in nearby_users.items():
                    self.assertLessEqual(data, hvsn_dist)
            
            total_users_hvsn += len(nearby_users)
            
            near_by_peers = neighbor_finder.find_by_normalization( user_info,
                        user_data, norm_dist)
            # Test that all differences in lat fall between 0 and 1
            for _, data in near_by_peers.items():
                self.assertGreaterEqual(data[0], 0)
                self.assertLess(data[0], 1)
            
            total_users_norm += len(near_by_peers)
        
        avg_hvsn = total_users_hvsn / len(user_data)
        avg_norm = total_users_norm / len(user_data)

        hvsn_stats = "Haversine distance " + str(hvsn_dist) + " feet" + "\n"
        hvsn_stats += "Avg numbers of nearby peers " + str(avg_hvsn) + "\n"
        print(hvsn_stats + "\n\n")
        
        norm_stats = "Normalization distance " + str(norm_dist) + "\n"
        norm_stats += "Avg number of nearby peers "+ str(avg_norm) + "\n"
        print(norm_stats + "\n\n")

        print("Test passed: %s" % inspect.stack()[0][3])
        
if __name__ == '__main__':
    unittest.main()
