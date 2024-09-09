from collections import deque, OrderedDict

def fifo(pages, capacity):
    s = set()
    indexes = deque()
    page_faults = 0

    for i in range(len(pages)):        
        if pages[i] not in s:
            if len(s) < capacity:
                    s.add(pages[i])
                    page_faults += 1
                    indexes.append(pages[i])
            else:
                    val = indexes.popleft()
                    s.remove(val)
                    s.add(pages[i])
                    indexes.append(pages[i])
                    page_faults += 1

    return page_faults

def lru(pages, capacity):
    s = set()
    indexes = OrderedDict()
    page_faults = 0

    for i in range(len(pages)):
        if pages[i] not in s:
            if len(s) < capacity:                
                s.add(pages[i])
                page_faults += 1
            else:
                if indexes:
                    lru_page = next(iter(indexes))
                    del indexes[lru_page]
                    s.remove(lru_page)
                    page_faults += 1
                s.add(pages[i])
            indexes[pages[i]] = i

    return page_faults

pages = [7, 0, 1, 2, 0, 3, 0, 4, 2, 3, 0, 3, 2]
capacity = 3

print("FIFO Page Faults: ", fifo(pages, capacity))
print("LRU Page Faults: ", lru(pages, capacity))


# Output

from collections import deque, OrderedDict

def fifo(pages, capacity):
    s, indexes, page_faults = set(), deque(), 0
    return sum(pages[i] not in s and (s.add(pages[i]), indexes.append(pages[i]), 1)
               if len(s) < capacity else (s.remove(indexes.popleft()), s.add(pages[i]), indexes.append(pages[i]), 1)
               for i in range(len(pages)))

def lru(pages, capacity):
    s, indexes, page_faults = set(), OrderedDict(), 0
    return sum(pages[i] not in s and (s.add(pages[i]), 1) if len(s) < capacity else (
               s.remove(next(iter(indexes))), del indexes[next(iter(indexes))], s.add(pages[i]), 1)
               for i in range(len(pages)))

pages = [7, 0, 1, 2, 0, 3, 0, 4, 2, 3, 0, 3, 2]
capacity = 3

print("Number of pages Python: ", len(pages))
print("FIFO Page Faults: ", fifo(pages, capacity))
print("LRU Page Faults: ", lru(pages, capacity))

