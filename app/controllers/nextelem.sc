val l =  List(1,2,3)

var index = 0
def getNextElement(data : List[Int]) = {
  index = index + 1
  l(index - 1)
}

print(getNextElement(l))
print(getNextElement(l))
print(getNextElement(l))
print(getNextElement(l))